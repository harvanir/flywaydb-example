# flywaydb example (MySQL)

**1. Prerequisite**

```bash
Database name : flywaydb
Username : flywaydb
Password : flywaydb

```

**2. Run maven for the first time**

Command:

```bash
mvn flyway:info -Dflyway.outOfOrder=true

```
Result:

```bash
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building flywaydb-example 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- flyway-maven-plugin:5.1.3:info (default-cli) @ flywaydb-example ---
[INFO] Flyway Community Edition 5.1.3 by Boxfuse
[INFO] Database: jdbc:mysql://localhost:3306/flywaydb (MySQL 5.5)
[INFO] Schema version: << Empty Schema >>
[INFO]
[INFO] +-----------+---------+---------------------+------+--------------+---------+
| Category  | Version | Description         | Type | Installed On | State   |
+-----------+---------+---------------------+------+--------------+---------+
| Versioned | 1       | Create person table | SQL  |              | Pending |
| Versioned | 2       | Add people          | SQL  |              | Pending |
| Versioned | 3       | Add people          | SQL  |              | Pending |
| Versioned | 4       | Create person table | SQL  |              | Pending |
| Versioned | 99      | Add people          | JDBC |              | Pending |
+-----------+---------+---------------------+------+--------------+---------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.085 s
[INFO] Finished at: 2018-07-01T23:00:49+07:00
[INFO] Final Memory: 9M/150M
[INFO] ------------------------------------------------------------------------
```

**3. Run maven to execute database migration**

Command:

```bash
mvn flyway:migrate -Dflyway.outOfOrder=true

```
Result:

```bash
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building flywaydb-example 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- flyway-maven-plugin:5.1.3:migrate (default-cli) @ flywaydb-example ---
[INFO] Flyway Community Edition 5.1.3 by Boxfuse
[INFO] Database: jdbc:mysql://localhost:3306/flywaydb (MySQL 5.5)
[INFO] Successfully validated 5 migrations (execution time 00:00.018s)
[INFO] Creating Schema History table: `flywaydb`.`flyway_schema_history`
[INFO] Current version of schema `flywaydb`: << Empty Schema >>
[WARNING] outOfOrder mode is active. Migration of schema `flywaydb` may not be reproducible.
[INFO] Migrating schema `flywaydb` to version 1 - Create person table
[INFO] Migrating schema `flywaydb` to version 2 - Add people
[INFO] Migrating schema `flywaydb` to version 3 - Add people
[INFO] Migrating schema `flywaydb` to version 4 - Create person table
[WARNING] DB: Table 'person' already exists (SQL State: 42S01 - Error Code: 1050)
[ERROR] Migration of schema `flywaydb` to version 4 - Create person table failed! Please restore backups and roll back database and code!
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.941 s
[INFO] Finished at: 2018-07-01T23:01:25+07:00
[INFO] Final Memory: 14M/212M
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.flywaydb:flyway-maven-plugin:5.1.3:migrate (default-cli) on project flywaydb-example: org.flywaydb.core.internal.command.DbMigrate$FlywayMigrateException:
[ERROR] Migration V4__Create_person_table.sql failed
[ERROR] --------------------------------------------
[ERROR] SQL State  : 42S01
[ERROR] Error Code : 1050
[ERROR] Message    : Table 'person' already exists
[ERROR] Location   : db/migration/sql/ddl/V4__Create_person_table.sql (D:\harvan\programming\java\github.com\harvanir\example\flywaydb-example\target\classes\db\migration\sql\ddl\V4__Create_person_table.sql)
[ERROR] Line       : 1
[ERROR] Statement  : create table PERSON (
[ERROR]     ID int not null,
[ERROR]     NAME varchar(100) not null
[ERROR] )
[ERROR]
[ERROR] -> [Help 1]
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR]
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoExecutionException
```

**4. Your process will return an error because there are 1 file conflict**

```bash
Existing valid script: "db/migration/sql/ddl/V1__Create_person_table.sql"
Invalid/duplicate script: "db/migration/sql/ddl/V4__Create_person_table.sql"
```

**5. Check unsuccessful process**

Command:

```bash
mvn flyway:info -Dflyway.outOfOrder=true
```

Result:

```bash
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building flywaydb-example 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- flyway-maven-plugin:5.1.3:info (default-cli) @ flywaydb-example ---
[INFO] Flyway Community Edition 5.1.3 by Boxfuse
[INFO] Database: jdbc:mysql://localhost:3306/flywaydb (MySQL 5.5)
[INFO] Schema version: 4
[INFO]
[INFO] +-----------+---------+---------------------+------+---------------------+---------+
| Category  | Version | Description         | Type | Installed On        | State   |
+-----------+---------+---------------------+------+---------------------+---------+
| Versioned | 1       | Create person table | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 2       | Add people          | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 3       | Add people          | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 4       | Create person table | SQL  | 2018-07-01 23:01:25 | Failed  |
| Versioned | 99      | Add people          | JDBC |                     | Pending |
+-----------+---------+---------------------+------+---------------------+---------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.127 s
[INFO] Finished at: 2018-07-01T23:02:31+07:00
[INFO] Final Memory: 13M/150M
[INFO] ------------------------------------------------------------------------
```

**6. To continue your next migrating process, do 2 steps bellow**

(1.) Repair command:

```bash
mvn flywaydb:repair
```

Result:

```bash
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building flywaydb-example 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- flyway-maven-plugin:5.1.3:repair (default-cli) @ flywaydb-example ---
[INFO] Flyway Community Edition 5.1.3 by Boxfuse
[INFO] Database: jdbc:mysql://localhost:3306/flywaydb (MySQL 5.5)
[INFO] Successfully repaired schema history table `flywaydb`.`flyway_schema_history` (execution time 00:00.162s).
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.297 s
[INFO] Finished at: 2018-07-01T23:05:50+07:00
[INFO] Final Memory: 14M/212M
[INFO] ------------------------------------------------------------------------
```

(2.) Delete 1 invalid/conflict file:

```bash
db/migration/sql/ddl/V4__Create_person_table.sql
```

**7. Check your latest migration status**

Command:

```bash
mvn flyway:info -Dflyway.outOfOrder=true
```

Result:

```bash
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building flywaydb-example 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- flyway-maven-plugin:5.1.3:info (default-cli) @ flywaydb-example ---
[INFO] Flyway Community Edition 5.1.3 by Boxfuse
[INFO] Database: jdbc:mysql://localhost:3306/flywaydb (MySQL 5.5)
[INFO] Schema version: 3
[INFO]
[INFO] +-----------+---------+---------------------+------+---------------------+---------+
| Category  | Version | Description         | Type | Installed On        | State   |
+-----------+---------+---------------------+------+---------------------+---------+
| Versioned | 1       | Create person table | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 2       | Add people          | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 3       | Add people          | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 99      | Add people          | JDBC |                     | Pending |
+-----------+---------+---------------------+------+---------------------+---------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 0.990 s
[INFO] Finished at: 2018-07-01T23:06:44+07:00
[INFO] Final Memory: 12M/150M
[INFO] ------------------------------------------------------------------------
```
**8. Run migration**

Command:

```bash
mvn flyway:migrate -Dflyway.outOfOrder=true
```

Result:

```bash
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building flywaydb-example 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- flyway-maven-plugin:5.1.3:migrate (default-cli) @ flywaydb-example ---
[INFO] Flyway Community Edition 5.1.3 by Boxfuse
[INFO] Database: jdbc:mysql://localhost:3306/flywaydb (MySQL 5.5)
[INFO] Successfully validated 4 migrations (execution time 00:00.036s)
[INFO] Current version of schema `flywaydb`: 3
[WARNING] outOfOrder mode is active. Migration of schema `flywaydb` may not be reproducible.
[INFO] Migrating schema `flywaydb` to version 99 - Add people
[INFO] Successfully applied 1 migration to schema `flywaydb` (execution time 00:00.110s)
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.352 s
[INFO] Finished at: 2018-07-01T23:07:22+07:00
[INFO] Final Memory: 11M/150M
[INFO] ------------------------------------------------------------------------
```

**9. Check status**

Command:

```bash
mvn flyway:info -Dflyway.outOfOrder=true
```

Result:

```bash
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building flywaydb-example 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- flyway-maven-plugin:5.1.3:info (default-cli) @ flywaydb-example ---
[INFO] Flyway Community Edition 5.1.3 by Boxfuse
[INFO] Database: jdbc:mysql://localhost:3306/flywaydb (MySQL 5.5)
[INFO] Schema version: 99
[INFO]
[INFO] +-----------+---------+---------------------+------+---------------------+---------+
| Category  | Version | Description         | Type | Installed On        | State   |
+-----------+---------+---------------------+------+---------------------+---------+
| Versioned | 1       | Create person table | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 2       | Add people          | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 3       | Add people          | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 99      | Add people          | JDBC | 2018-07-01 23:07:22 | Success |
+-----------+---------+---------------------+------+---------------------+---------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.076 s
[INFO] Finished at: 2018-07-01T23:09:37+07:00
[INFO] Final Memory: 11M/150M
[INFO] ------------------------------------------------------------------------
```

**10. Rename migration file**

```bash
1. "db/migration/sql/dml/_V4__SyncMeta.sql" to "db/migration/sql/dml/V4__SyncMeta.sql"
2. "db/migration/sql/dml/V99__Add_people.java" to "db/migration/sql/dml/V5__Add_people.java"
```

**11. Update file V5__Add_people.java**

Change "VERSION" variable value from "'99'" to "'5'"

**12. Check status**

Command:

```bash
mvn flyway:info -Dflyway.outOfOrder=true
```

Result:

```bash
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building flywaydb-example 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- flyway-maven-plugin:5.1.3:info (default-cli) @ flywaydb-example ---
[INFO] Flyway Community Edition 5.1.3 by Boxfuse
[INFO] Database: jdbc:mysql://localhost:3306/flywaydb (MySQL 5.5)
[INFO] Schema version: 99
[INFO]
[INFO] +-----------+---------+---------------------+------+---------------------+---------+
| Category  | Version | Description         | Type | Installed On        | State   |
+-----------+---------+---------------------+------+---------------------+---------+
| Versioned | 1       | Create person table | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 2       | Add people          | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 3       | Add people          | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 99      | Add people          | JDBC | 2018-07-01 23:07:22 | Future  |
| Versioned | 4       | SyncMeta            | SQL  |                     | Pending |
| Versioned | 5       | Add people          | JDBC |                     | Pending |
+-----------+---------+---------------------+------+---------------------+---------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.161 s
[INFO] Finished at: 2018-07-02T00:23:02+07:00
[INFO] Final Memory: 14M/212M
[INFO] ------------------------------------------------------------------------
```

**13. Run migration**

Command:

```bash
mvn flyway:migrate -Dflyway.outOfOrder=true
```

Result:

```bash
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building flywaydb-example 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- flyway-maven-plugin:5.1.3:migrate (default-cli) @ flywaydb-example ---
[INFO] Flyway Community Edition 5.1.3 by Boxfuse
[INFO] Database: jdbc:mysql://localhost:3306/flywaydb (MySQL 5.5)
[INFO] Successfully validated 6 migrations (execution time 00:00.023s)
[INFO] Current version of schema `flywaydb`: 99
[WARNING] outOfOrder mode is active. Migration of schema `flywaydb` may not be reproducible.
[WARNING] Schema `flywaydb` has a version (99) that is newer than the latest available migration (5) !
[INFO] Migrating schema `flywaydb` to version 4 - SyncMeta [out of order]
[WARNING] Schema `flywaydb` has a version (99) that is newer than the latest available migration (5) !
[INFO] Migrating schema `flywaydb` to version 5 - Add people [out of order]
[WARNING] Schema `flywaydb` has a version (99) that is newer than the latest available migration (5) !
[INFO] Successfully applied 2 migrations to schema `flywaydb` (execution time 00:00.270s)
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.280 s
[INFO] Finished at: 2018-07-02T00:24:19+07:00
[INFO] Final Memory: 9M/150M
[INFO] ------------------------------------------------------------------------
```

**14. Check status**

Command:

```bash
mvn flyway:info -Dflyway.outOfOrder=true
```

Result:

```bash
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building flywaydb-example 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- flyway-maven-plugin:5.1.3:info (default-cli) @ flywaydb-example ---
[INFO] Flyway Community Edition 5.1.3 by Boxfuse
[INFO] Database: jdbc:mysql://localhost:3306/flywaydb (MySQL 5.5)
[INFO] Schema version: 5
[INFO]
[INFO] +-----------+---------+---------------------+------+---------------------+---------+
| Category  | Version | Description         | Type | Installed On        | State   |
+-----------+---------+---------------------+------+---------------------+---------+
| Versioned | 1       | Create person table | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 2       | Add people          | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 3       | Add people          | SQL  | 2018-07-01 23:01:25 | Success |
| Versioned | 4       | SyncMeta            | SQL  | 2018-07-02 00:24:19 | Success |
| Versioned | 5       | Add people          | JDBC | 2018-07-02 00:24:19 | Success |
+-----------+---------+---------------------+------+---------------------+---------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 0.988 s
[INFO] Finished at: 2018-07-02T00:25:57+07:00
[INFO] Final Memory: 11M/150M
[INFO] ------------------------------------------------------------------------
```
