package demo;

import demo.config.JpaConfig;
import demo.config.HsqlDataSource;
import demo.dto.Person;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class AuditTester2 {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx =
              new AnnotationConfigApplicationContext(
                      JpaConfig.class, HsqlDataSource.class);

        EntityManagerFactory factory = ctx.getBean(EntityManagerFactory.class);
        EntityManager em = factory.createEntityManager();

        em.getTransaction().begin();

        var person = new Person("Alice Smith");
        person.addPhone("123");
        em.persist(person);

        em.getTransaction().commit();

        em.getTransaction().begin();

        person.setName("Alice Jones");

        em.getTransaction().commit();

        em.close();
    }
}

