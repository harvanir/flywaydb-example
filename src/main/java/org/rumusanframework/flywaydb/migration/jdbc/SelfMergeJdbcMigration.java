/*
 * Copyright 2018-2021 the original author or authors.
 */

package org.rumusanframework.flywaydb.migration.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
 * Self merge JdbcMigration. Will never duplicate into table
 * <code>flyway_schema_history</code> and will never re-execute while migrating.
 *
 * @author Harvan Irsyadi
 * @version 1.0.0
 * @since 1.0.0 (1 Jul 2018)
 */
public abstract class SelfMergeJdbcMigration extends BaseJavaMigration { // NOSONAR

    @Override
    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        try (Statement select = connection.createStatement()) {
            String queryCheck = "select * from flyway_schema_history where version = " +
                    getVersion() +
                    " order by installed_rank";

            try (ResultSet rsHistory = select.executeQuery(queryCheck)) {
                if (!rsHistory.next()) {
                    doMigrationScript(connection);
                } else {
                    doHouseKeeping(connection, rsHistory);
                }
            }
        }
    }

    protected abstract void doMigrationScript(Connection connection) throws Exception; // NOSONAR

    private void doHouseKeeping(Connection connection, ResultSet rsHistory) throws Exception { // NOSONAR
        try (Statement selectStatementCount = connection.createStatement()) {
            String selectCount = "select count(1) from flyway_schema_history where version = " +
                    getVersion();

            try (ResultSet rows2 = selectStatementCount.executeQuery(selectCount)) {
                if (rows2.next()) {
                    int count = rsHistory.getInt(1);

                    if (count > 1) {
                        try (Statement delete = connection.createStatement()) {
                            int rank = rsHistory.getInt("installed_rank");
                            String deleteSql = "delete from flyway_schema_history where installed_rank = " +
                                    rank;

                            delete.execute(deleteSql);
                        }
                    }
                }
            }
        }
    }
}