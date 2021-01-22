package com.gufli.bookshelf.database;

public abstract class SqlRepository {

    protected final SqlDatabase database;

    public SqlRepository(SqlDatabase database) {
        this.database = database;
    }

}
