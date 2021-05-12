package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.util.JsonParser;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
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
        return sqlHelper.transactionalExecute(connection -> {
                    Map<String, Resume> resumes = new LinkedHashMap<>();

                    try (PreparedStatement ps = connection.prepareStatement("SELECT * from resume ORDER BY full_name, uuid")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString("uuid");
                            if (!resumes.containsKey(uuid)) {
                                resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                            }
                        }
                    }

                    try (PreparedStatement ps = connection.prepareStatement("SELECT * from contact")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            Resume resume = resumes.get(rs.getString("resume_uuid"));
                            addContactToResume(rs, resume);
                        }
                    }

                    try (PreparedStatement ps = connection.prepareStatement("SELECT * from section")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            Resume resume = resumes.get(rs.getString("resume_uuid"));
                            addSectionToResume(rs, resume);
                        }
                    }
                    return new ArrayList<>(resumes.values());
                });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(connection -> {
                    String uuid = resume.getUuid();
                    try (PreparedStatement ps = connection.prepareStatement("UPDATE resume SET full_name=? WHERE uuid =?")) {
                        ps.setString(1, resume.getFullName());
                        ps.setString(2, uuid);
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(uuid);
                        }
                    }
                    deleteContactsAndSections(connection, uuid);
                    insertContacts(connection, resume);
                    insertSections(connection, resume);
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
                    insertContacts(connection, resume);
                    insertSections(connection, resume);
                    return null;
                }
        );
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(connection -> {
            Resume resume;
            try (PreparedStatement ps = connection.prepareStatement("SELECT * from resume where uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, rs.getString("full_name"));
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT * from contact where resume_uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addContactToResume(rs, resume);
                }
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT * from section where resume_uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSectionToResume(rs, resume);
                }
            }
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

    private void deleteContactsAndSections(Connection connection, String uuid) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE from contact where resume_uuid =?")) {
            ps.setString(1, uuid);
            ps.execute();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE from section where resume_uuid =?")) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }


    private void insertContacts(Connection connection, Resume resume) throws SQLException {
        Map<ContactType, String> contacts = resume.getContacts();
        if (contacts.size() != 0) {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?,?,?)")) {
                for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, entry.getKey().name());
                    ps.setString(3, entry.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    private void insertSections(Connection connection, Resume resume) throws SQLException {
        Map<SectionType, AbstractSection> sections = resume.getSections();
        if (sections.size() != 0) {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO section(resume_uuid, type, value) VALUES (?,?,?)")) {
                for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                    SectionType type = entry.getKey();
                    AbstractSection value = entry.getValue();
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, type.name());
                    ps.setString(3, JsonParser.write(value, AbstractSection.class));
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    private void addContactToResume(ResultSet rs, Resume resume) throws SQLException{
        String value = rs.getString("value");
        if (value != null) {
            resume.setContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    private void addSectionToResume(ResultSet rs, Resume resume) throws SQLException{
        String content = rs.getString("value");
        if (content != null) {
            SectionType sectionType = SectionType.valueOf(rs.getString("type"));
            resume.setSections(sectionType, JsonParser.read(content, AbstractSection.class));
        }
    }

}

