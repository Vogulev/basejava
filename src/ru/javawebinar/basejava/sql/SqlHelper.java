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
        execute(sql, PreparedStatement::execute);
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

    public <T> T transactionalExecute(SqlTransaction<T> executor) {
        try (Connection connection = connectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                T res = executor.execute(connection);
                connection.commit();
                return res;
            } catch (SQLException e) {
                connection.rollback();
                checkException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
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
