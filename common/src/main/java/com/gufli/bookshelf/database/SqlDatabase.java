package com.gufli.bookshelf.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlDatabase {

    void close();

    boolean isClosed();

    Connection getConnection() throws SQLException;

}
