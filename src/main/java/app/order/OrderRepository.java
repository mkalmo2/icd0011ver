package app.order;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OrderRepository {

    private JdbcTemplate template;

    public OrderRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Transactional
    public void save(Order order) {
        if (order.getId() == null) {
            insert(order);
        } else {
            update(order);
        }
    }

    private void insert(Order order) {
        String sql = "INSERT INTO orders (number, version) " +
                     "VALUES (?, 1)";
    }

    private void update(Order order) {
        String lockQuery = "SELECT * FROM orders WHERE id = ? FOR UPDATE";
        String copyQuery = "INSERT INTO orders_history(id, number, version)" +
                           "  SELECT id, number, version" +
                           "  FROM orders WHERE id = ?";
        String updateQuery = "UPDATE orders " +
                             "  SET number = ?, version = version + 1 " +
                             "WHERE id = ?";
    }


}
