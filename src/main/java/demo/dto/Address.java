package demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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
