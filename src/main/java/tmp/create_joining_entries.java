package tmp;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

public class create_joining_entries extends BaseJavaMigration {

    @Override
    public void migrate(Context context) {

        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);

        JdbcClient jdbcClient = JdbcClient.create(ds);

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("person_phone")
                .usingColumns("person_id", "phone_id");

        String selectQuery = "SELECT person_id, id AS phone_id " +
                             "  FROM phone";

    }
}