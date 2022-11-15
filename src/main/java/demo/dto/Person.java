package demo.dto;

import lombok.*;
import org.hibernate.envers.Audited;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Audited
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

}
