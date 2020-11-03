package app.invoice;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class InvoiceRepository {

    private JdbcTemplate template;

    public InvoiceRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Transactional
    public void save(Invoice invoice) {
        if (invoice.getId() == null) {
            insert(invoice);
        } else {
            update(invoice);
        }
    }

    private void insert(Invoice invoice) {
        String sql = "INSERT INTO invoice (id, number, start_date) " +
                     "VALUES (nextval('seq_invoice'), ?, now())";

    }

    private void update(Invoice invoice) {
        String lockQuery = "SELECT * FROM invoice WHERE id = ? FOR UPDATE";
        String copyQuery = "INSERT INTO invoice(id, number, start_date, end_date)" +
                           "  SELECT id, number, start_date, now()" +
                           "  FROM invoice WHERE id = ? AND end_date IS NULL";
        String updateQuery = "UPDATE invoice SET number = ?, start_date = now() " +
                             "WHERE id = ? AND end_date IS NULL";
    }

    public List<Invoice> findById(Long invoiceId) {
        String sql = "SELECT id, number " +
                     "FROM invoice " +
                     "WHERE id = ? AND end_date IS NULL";

        return null;
    }

    public List<Invoice> findById(Long invoiceId, LocalDateTime moment) {
        String sql = "SELECT id, number FROM invoice " +
                     "WHERE id = ? " +
                     "  AND start_date <= ? " +
                     "  AND (end_date > ? OR end_date IS NULL)";

        return null;
    }


}
