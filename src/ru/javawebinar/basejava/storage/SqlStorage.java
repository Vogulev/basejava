package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;
    private SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", (ps, emptyPs) -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });

/*        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT count(*) FROM resume")) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new StorageException(e);
        }*/
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumeList = new ArrayList<>();
        sqlHelper.execute("SELECT * from resume r", (ps, emptyPs) -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumeList.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }
            return null;
        });

/*        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * from resume r")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumeList.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }*/
        return resumeList;
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.execute("SELECT * from resume r where r.uuid =?","UPDATE resume SET uuid =?, full_name=? WHERE uuid =?", (selectPs, updatePs) -> {
            String uuid = resume.getUuid();
            selectPs.setString(1, uuid);
            ResultSet rs = selectPs.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            updatePs.setString(1, uuid);
            updatePs.setString(2, resume.getFullName());
            updatePs.setString(3, uuid);
            updatePs.execute();
            return null;
        });

/*        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement selectPs = connection.prepareStatement("SELECT * from resume r where r.uuid =?");
             PreparedStatement updatePs = connection.prepareStatement("UPDATE resume SET uuid =?, full_name=? WHERE uuid =?")) {
            String uuid = resume.getUuid();
            selectPs.setString(1, uuid);
            ResultSet rs = selectPs.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            updatePs.setString(1, uuid);
            updatePs.setString(2, resume.getFullName());
            updatePs.setString(3, uuid);
            updatePs.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }*/
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.execute("SELECT * from resume r where r.uuid =?", "INSERT INTO resume(uuid, full_name) VALUES (?,?)", (selectPs, savePs) -> {
            String uuid = resume.getUuid();
            selectPs.setString(1, uuid);
            ResultSet rs = selectPs.executeQuery();
            if (rs.next()) {
                throw new ExistStorageException(uuid);
            }
            savePs.setString(1, uuid);
            savePs.setString(2, resume.getFullName());
            savePs.execute();
            return null;
        });

       /* try (Connection connection = connectionFactory.getConnection();
             PreparedStatement selectPs = connection.prepareStatement("SELECT * from resume r where r.uuid =?");
             PreparedStatement savePs = connection.prepareStatement("INSERT INTO resume(uuid, full_name) VALUES (?,?)")) {
            String uuid = resume.getUuid();
            selectPs.setString(1, uuid);
            ResultSet rs = selectPs.executeQuery();
            if (rs.next()) {
                throw new ExistStorageException(uuid);
            }
            savePs.setString(1, uuid);
            savePs.setString(2, resume.getFullName());
            savePs.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }*/
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("SELECT * from resume r where r.uuid =?", (ps, emptyPs) -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        });
    }
/*        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * from resume r where r.uuid =?")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }*/

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("SELECT * from resume r where r.uuid =?", "DELETE from resume r where r.uuid =?", (selectPs, deletePs) -> {
            selectPs.setString(1, uuid);
            ResultSet rs = selectPs.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            deletePs.setString(1, uuid);
            deletePs.execute();
            return null;
        });

        /*try (Connection connection = connectionFactory.getConnection();
             PreparedStatement selectPs = connection.prepareStatement("SELECT * from resume r where r.uuid =?");
             PreparedStatement deletePs = connection.prepareStatement("DELETE from resume r where r.uuid =?")) {
            selectPs.setString(1, uuid);
            ResultSet rs = selectPs.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            deletePs.setString(1, uuid);
            deletePs.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }*/
    }
}

