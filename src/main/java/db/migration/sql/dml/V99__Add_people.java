package db.migration.sql.dml;

import java.sql.Connection;
import java.sql.Statement;

import org.rumusanframework.flywaydb.migration.jdbc.SelfMergeJdbcMigration;

public class V99__Add_people extends SelfMergeJdbcMigration { // NOSONAR
	private static final String VERSION = "'99'";

	@Override
	protected String getVersion() {
		return VERSION;
	}

	@Override
	protected void doMigrationScript(Connection connection) throws Exception { // NOSONAR
		try (Statement insert = connection.createStatement()) {
			insert.execute("insert into PERSON (ID, NAME) values (7, 'Axel3')");
		}
		try (Statement insert = connection.createStatement()) {
			insert.execute("insert into PERSON (ID, NAME) values (8, 'Mr. Foo3')");
		}
		try (Statement insert = connection.createStatement()) {
			insert.execute("insert into PERSON (ID, NAME) values (9, 'Ms. Bar3')");
		}
	}
}