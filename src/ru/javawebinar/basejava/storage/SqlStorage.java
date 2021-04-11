package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public int size() {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT count(*) FROM resume")) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void clear() {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM resume")) {
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumeList = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * from resume r")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumeList.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
        return resumeList;
    }

    @Override
    public void update(Resume resume) {
        try (Connection connection = connectionFactory.getConnection();
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
        }
    }

    @Override
    public void save(Resume resume) {
        try (Connection connection = connectionFactory.getConnection();
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
        }
    }

    @Override
    public Resume get(String uuid) {
        try (Connection connection = connectionFactory.getConnection();
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
    }

    @Override
    public void delete(String uuid) {
        try (Connection connection = connectionFactory.getConnection();
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
        }
    }
}
