package demo;

import demo.config.JpaConfig;
import demo.config.HsqlDataSource;
import demo.dto.Person;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.Date;

public class AuditTester3 {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx =
              new AnnotationConfigApplicationContext(
                      JpaConfig.class, HsqlDataSource.class);

        EntityManagerFactory factory = ctx.getBean(EntityManagerFactory.class);
        EntityManager em = factory.createEntityManager();

        em.getTransaction().begin();

        var person = new Person("Alice Smith");
        person.addPhone("123");
        person.addPhone("456");
        em.persist(person);

        em.getTransaction().commit();

        Date date = new Date();

        em.getTransaction().begin();

        person.setName("Alice Jones");
        person.removePhone("123");
        person.addPhone("789");

        em.getTransaction().commit();

        Person p1 = em.find(Person.class, 1L);

        AuditReader reader = AuditReaderFactory.get(em);
        Person p2 = reader.find(Person.class, 1L, date);

        System.out.println(p1);
        System.out.println(p2);

        em.close();
    }
}

