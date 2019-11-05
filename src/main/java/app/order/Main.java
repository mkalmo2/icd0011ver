package app.order;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(OrderConfig.class);

        OrderRepository repo = ctx.getBean(OrderRepository.class);

        repo.save(new Order(null, "o123_v1"));

        repo.save(new Order(1L, "o123_v2"));

        repo.save(new Order(1L, "o123_v3"));

    }
}
