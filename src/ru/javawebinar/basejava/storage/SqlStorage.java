package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", (ps) -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumeList = new ArrayList<>();
        sqlHelper.execute("SELECT * from resume ORDER BY full_name, uuid", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumeList.add(get(rs.getString("uuid")));
            }
            return null;
        });
        return resumeList;
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(connection -> {
                    try (PreparedStatement ps = connection.prepareStatement("UPDATE resume SET full_name=? WHERE uuid =?")) {
                        ps.setString(1, resume.getFullName());
                        ps.setString(2, resume.getUuid());
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(resume.getUuid());
                        }
                    }
                    deleteContacts(resume.getUuid());
                    insertContacts(connection, resume);
                    return null;
                }
        );
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(connection -> {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO resume(uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, resume.getFullName());
                        ps.execute();
                    }
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?,?,?)")) {
                        for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                            ps.setString(1, resume.getUuid());
                            ps.setString(2, entry.getKey().name());
                            ps.setString(3, entry.getValue());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                    return null;
                }
        );
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        "    SELECT * from resume r" +
                        " LEFT JOIN contact c" +
                        "        ON r.uuid = c.resume_uuid" +
                        "     WHERE r.uuid =? ",
                (ps) -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, rs.getString("full_name"));
                    do {
                        String value = rs.getString("value");
                        if (value != null) {
                            resume.setContact(ContactType.valueOf(rs.getString("type")), value);
                        }
                    } while (rs.next());
                    return resume;
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE from resume r where r.uuid =?", (ps) -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    private void deleteContacts(String uuid) {
        sqlHelper.execute("DELETE from contact where resume_uuid =?", (ps) -> {
            ps.setString(1, uuid);
            ps.execute();
            return null;
        });
    }

    private void insertContacts(Connection connection, Resume resume) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

}

