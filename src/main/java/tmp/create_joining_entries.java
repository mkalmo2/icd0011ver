package tmp;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class create_joining_entries extends BaseJavaMigration {
    public void migrate(Context context) {

        var template = new JdbcTemplate(
                new SingleConnectionDataSource(context.getConnection(), true));

        String selectQuery = "SELECT person_id, id AS phone_id " +
                             "  FROM phone";

        String insertQuery = "INSERT INTO person_phone " +
                             "  (person_id, phone_id)" +
                             "  VALUES (?, ?)";

    }
}