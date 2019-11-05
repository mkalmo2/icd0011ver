package app.invoice;

import app.CommonConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(InvoiceConfig.class);

        InvoiceRepository repo = ctx.getBean(InvoiceRepository.class);

        repo.save(new Invoice(null, "invoice_v1"));

    }

}
