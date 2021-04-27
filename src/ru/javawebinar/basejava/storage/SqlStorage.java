package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.*;

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
        Map<String, Resume> resumes = new HashMap<>();
        sqlHelper.transactionalExecute(connection -> {
                    try (PreparedStatement ps = connection.prepareStatement("SELECT * from resume")) {
                        ResultSet rs = ps.executeQuery();
                        if (!rs.next()) {
                            throw new NotExistStorageException(null);
                        }
                        do {
                            String uuid = rs.getString("uuid");
                            if (!resumes.containsKey(uuid)) {
                                resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                            }
                        } while (rs.next());
                    }
                    try (PreparedStatement ps = connection.prepareStatement("SELECT * from contact")) {
                        ResultSet rs = ps.executeQuery();
                        if (!rs.next()) {
                            throw new NotExistStorageException(null);
                        }
                        do {
                            String uuid = rs.getString("resume_uuid");
                            for (Map.Entry<String, Resume> pair : resumes.entrySet()) {
                                if (pair.getKey().equals(uuid)) {
                                    addContactToResume(rs.getString("type"), rs.getString("value"), pair.getValue());
                                }
                            }
                        } while (rs.next());
                    }
                    try (PreparedStatement ps = connection.prepareStatement("SELECT * from section")) {
                        ResultSet rs = ps.executeQuery();
                        if (!rs.next()) {
                            throw new NotExistStorageException(null);
                        }
                        do {
                            String uuid = rs.getString("resume_uuid");
                            for (Map.Entry<String, Resume> pair : resumes.entrySet()) {
                                if (pair.getKey().equals(uuid)) {
                                    addSectionToResume(rs.getString("type"), rs.getString("value"), pair.getValue());
                                }
                            }
                        } while (rs.next());
                    }
                    return null;
                }
        );
        List<Resume> resumesList = new ArrayList<>(resumes.values());
        Collections.sort(resumesList);
        return resumesList;
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
                    addContactToResume(rs.getString("type"), rs.getString("value"), resume);
                }
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT * from section where resume_uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String st1 = rs.getString("type");
                    String st2 = rs.getString("value");
                    addSectionToResume(rs.getString("type"), rs.getString("value"), resume);
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
                    if (type == SectionType.ACHIEVEMENT || type == SectionType.QUALIFICATIONS) {
                        ps.setString(3,
                                String.join("\n", ((ListSection) value).getContentList()));
                    } else {
                        ps.setString(3, String.valueOf(value));
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    private void addContactToResume(String type, String value, Resume resume) {
        if (value != null) {
            resume.setContact(ContactType.valueOf(type), value);
        }
    }

    private void addSectionToResume(String type, String value, Resume resume) {
        if (value != null) {
            SectionType sectionType = SectionType.valueOf(type);
            if (sectionType == SectionType.OBJECTIVE || sectionType == SectionType.PERSONAL) {
                resume.setSections(sectionType, new TextSection(value.trim()));
            } else {
                List<String> sectionList = new ArrayList<>();
                for (String string : List.of(value.split("\n"))) {
                    sectionList.add(string.trim());
                }
                resume.setSections(sectionType, new ListSection(sectionList));
            }
        }
    }
}

