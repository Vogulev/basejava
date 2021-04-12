package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ABlockOfCode<T> {
    T execute(PreparedStatement ps1, PreparedStatement ps2) throws SQLException;
}
