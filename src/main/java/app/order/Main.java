package app.order;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(OrderConfig.class);

        OrderRepository repo = ctx.getBean(OrderRepository.class);

        repo.save(new Order(null, "o1_v1"));

    }
}
