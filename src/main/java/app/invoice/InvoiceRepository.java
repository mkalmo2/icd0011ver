package app.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class InvoiceRepository {

    @Autowired
    private JdbcTemplate template;

    @Transactional
    public void save(Invoice invoice) {
        if (invoice.getId() == null) {
            insert(invoice);
        } else {
            update(invoice);
        }
    }

    public List<Invoice> findById(Long invoiceId) {
        String sql = "SELECT id, number " +
                     "FROM invoice " +
                     "WHERE id = ? AND end_date IS NULL";

        return template.query(sql,
                new Object[] { invoiceId },
                new BeanPropertyRowMapper<>(Invoice.class));
    }

    public List<Invoice> findById(Long invoiceId, LocalDateTime moment) {
        String sql = "SELECT id, number FROM invoice " +
                     "WHERE id = ? " +
                     "  AND start_date <= ? " +
                     "  AND (end_date > ? OR end_date IS NULL)";

        return template.query(sql,
                new Object[] { invoiceId, moment, moment },
                new BeanPropertyRowMapper<>(Invoice.class));
    }

    private void update(Invoice invoice) {

        String copyQuery = "INSERT INTO invoice(id, number, start_date, end_date)" +
                           "  SELECT id, number, start_date, now()" +
                           "  FROM invoice WHERE id = ?";

        template.update(copyQuery, invoice.getId());

        String updateQuery = "UPDATE invoice SET number = ? " +
                             "WHERE id = ? AND end_date IS NULL";

        template.update(updateQuery, invoice.getNumber(), invoice.getId());
    }

    private void insert(Invoice invoice) {
        String sql = "INSERT INTO invoice (id, number, start_date) " +
                     "VALUES (nextval('seq_invoice'), ?, now())";

        template.update(sql, invoice.getNumber());
    }

}
