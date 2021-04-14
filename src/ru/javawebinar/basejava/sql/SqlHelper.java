package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.ExistStorageException;
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
            return aBlockOfCode.execute(ps);
        } catch (SQLException e) {
            checkException(e);
        }
        return null;
    }

    public void checkException(SQLException e) {
        if (e.getSQLState().equals("23505")) {
            throw new ExistStorageException("uuid");
        }
        throw new StorageException(e);
    }
}
