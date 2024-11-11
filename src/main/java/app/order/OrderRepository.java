package app.order;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OrderRepository {

    private final JdbcClient jdbcClient;

    public OrderRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Transactional
    public void save(Order order) {
        if (order.id() == null) {
            insert(order);
        } else {
            update(order);
        }
    }

    private void insert(Order order) {
        String sql = """
                     INSERT INTO orders (number, version)
                     VALUES (:number, 1)""";


    }

    private void update(Order order) {
        String lockQuery = "SELECT * FROM orders WHERE id = :id FOR UPDATE";

        String copyQuery = """
                              INSERT INTO orders_history(id, number, version)
                                SELECT id, number, version
                                FROM orders WHERE id = :id""";

        String updateQuery = """
                             UPDATE orders
                               SET number = :number, version = version + 1
                               WHERE id = :id""";



    }


}
