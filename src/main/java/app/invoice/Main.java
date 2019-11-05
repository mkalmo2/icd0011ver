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

        repo.save(new Invoice(100L, "invoice_v2"));

        System.out.println(repo.findById(100L));

//        LocalDateTime moment = LocalDate.parse("2019-01-01").atStartOfDay();
//
//        System.out.println(repo.findById(1L));
//
//        System.out.println(repo.findById(1L, moment));

    }

}
