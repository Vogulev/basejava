package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void execute(String sql) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public <T> T execute(String sql, ABlockOfCode<T> aBlockOfCode) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            return aBlockOfCode.execute(ps, null);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public <T> void execute(String sql1, String sql2, ABlockOfCode<T> aBlockOfCode) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement selectPs = connection.prepareStatement(sql1);
             PreparedStatement executePs = connection.prepareStatement(sql2)) {
            aBlockOfCode.execute(selectPs, executePs);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

/*    public <T> void checkForException(String uuid, boolean condition) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * from resume r where r.uuid =?")) {
            ps.setString(1, uuid);
            if (condition) {
                throw new NotExistStorageException(uuid);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }*/
}
