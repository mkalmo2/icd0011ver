package app.order;

import app.CommonConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Import(CommonConfig.class)
@ComponentScan(basePackages = { "app.order" })
public class OrderConfig {

    @Bean
    public ResourceDatabasePopulator schemaFileName() {
        return new ResourceDatabasePopulator(
                new ClassPathResource("order_schema.sql"));
    }



}