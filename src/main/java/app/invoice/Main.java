package app.invoice;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(InvoiceConfig.class);

        InvoiceRepository repo = ctx.getBean(InvoiceRepository.class);

        repo.save(new Invoice(null, "invoice_v1"));

    }

}
