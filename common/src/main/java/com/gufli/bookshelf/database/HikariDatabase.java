package com.gufli.bookshelf.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariDatabase implements SqlDatabase {

    private final HikariDataSource pool;

    public HikariDatabase(String url, String username, String password, int maxPoolSize) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);

        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password != null ? password : "");

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("useLocalSessionState", true);
        config.addDataSourceProperty("useLocalTransactionState", true);
        config.addDataSourceProperty("rewriteBatchedStatements", true);
        config.addDataSourceProperty("cacheResultSetMetadata", true);
        config.addDataSourceProperty("cacheServerConfiguration", true);
        config.addDataSourceProperty("elideSetAutoCommits", true);
        config.addDataSourceProperty("maintainTimeStats", true);

        config.setMaximumPoolSize(maxPoolSize);
        config.setMaxLifetime(1800000);
        config.setMinimumIdle(4);
        config.setIdleTimeout(10000);
        config.setLeakDetectionThreshold(5000);
        config.setAutoCommit(true);

        pool = new HikariDataSource(config);
    }

    @Override
    public void close() {
        if ( pool != null ) {
            pool.close();
        }
    }

    @Override
    public boolean isClosed() {
        return pool != null && pool.isClosed();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

}
