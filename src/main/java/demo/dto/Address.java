package demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
@Audited
public class Address {

    @Id
    @GeneratedValue
    private Long id;

    private String street;

    public Address(String street) {
        this.street = street;
    }
}
