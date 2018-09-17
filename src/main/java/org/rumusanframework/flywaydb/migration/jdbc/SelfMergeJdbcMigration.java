/*
 * Copyright 2018-2018 the original author or authors.
 */

package org.rumusanframework.flywaydb.migration.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

/**
 * Self merge JdbcMigration. Never duplicate into table
 * <code>flyway_schema_history</code> and never re-executed while migrating
 * 
 * @author Harvan Irsyadi
 * @version 1.0.0
 * @since 1.0.0 (1 Jul 2018)
 *
 */
public abstract class SelfMergeJdbcMigration implements JdbcMigration { // NOSONAR
	protected abstract String getVersion();

	@Override
	public void migrate(Connection connection) throws Exception {
		try (Statement select = connection.createStatement()) {
			StringBuilder queryCheck = new StringBuilder();

			queryCheck.append("select * from flyway_schema_history where version = ");
			queryCheck.append(getVersion());
			queryCheck.append(" order by installed_rank");

			try (ResultSet rsHistory = select.executeQuery(queryCheck.toString())) {
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
			StringBuilder selectCount = new StringBuilder();

			selectCount.append("select count(1) from flyway_schema_history where version = ");
			selectCount.append(getVersion());

			try (ResultSet rows2 = selectStatementCount.executeQuery(selectCount.toString())) {
				if (rows2.next()) {
					int count = rsHistory.getInt(1);

					if (count > 1) {
						try (Statement delete = connection.createStatement()) {
							int rank = rsHistory.getInt("installed_rank");
							StringBuilder deleteSql = new StringBuilder();

							deleteSql.append("delete from flyway_schema_history where installed_rank = ");
							deleteSql.append(rank);

							delete.execute(deleteSql.toString());
						}
					}
				}
			}
		}
	}
}