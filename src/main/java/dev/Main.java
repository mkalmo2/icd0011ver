package dev;

import demo.config.HsqlDataSource;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        var ctx = new AnnotationConfigApplicationContext(HsqlDataSource.class);

        DataSource ds = ctx.getBean(DataSource.class);

        Context context = crateContext(ds);

        // new V4_3__create_joining_entries().migrate(context);
    }

    private static Context crateContext(DataSource ds) {
        return new Context() {
            @Override
            public Configuration getConfiguration() {
                return null;
            }

            @Override
            public Connection getConnection() {
                try {
                    return ds.getConnection();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
