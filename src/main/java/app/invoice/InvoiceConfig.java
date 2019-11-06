package app.invoice;

import app.CommonConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@Import(CommonConfig.class)
@ComponentScan(basePackages = { "app.invoice" })
public class InvoiceConfig {

    @Bean
    public ResourceDatabasePopulator schemaFileName() {
        return new ResourceDatabasePopulator(
                new ClassPathResource("invoice_schema.sql"));
    }

}