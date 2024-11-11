package app.invoice;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class InvoiceRepository {

    private final JdbcClient jdbcClient;

    public InvoiceRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Transactional
    public void save(Invoice invoice) {
        if (invoice.id() == null) {
            insert(invoice);
        } else {
            update(invoice);
        }
    }

    private void insert(Invoice invoice) {
        String sql = """
                     INSERT INTO invoice (id, number, start_date)
                     VALUES (nextval('seq_invoice'), :number, now())""";

    }

    private void update(Invoice invoice) {
        String lockQuery = "SELECT * FROM invoice WHERE id = :id FOR UPDATE";

        String copyQuery = """
                           INSERT INTO invoice(id, number, start_date, end_date)
                              SELECT id, number, start_date, now()
                              FROM invoice WHERE id = :id AND end_date IS NULL""";

        String updateQuery = """
                             UPDATE invoice SET number = :number, start_date = now()
                             WHERE id = :id AND end_date IS NULL""";

    }

    public Optional<Invoice> findById(Long invoiceId) {
        String sql = """
                        SELECT id, number
                        FROM invoice
                        WHERE id = :id AND end_date IS NULL""";

        return null;
    }

    public Optional<Invoice> findById(Long invoiceId, LocalDateTime moment) {
        // SqlBuilder builder = ...

        return null;
    }
}
