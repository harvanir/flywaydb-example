update flyway_schema_history
set version = 5, script = 'db.migration.sql.dml.V5__Add_people'
where version = '99';
commit;