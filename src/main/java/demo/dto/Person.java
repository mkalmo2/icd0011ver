package demo.dto;

import lombok.*;
import org.hibernate.envers.Audited;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "person_id")
    private List<Phone> phones = new ArrayList<>();

    public void addPhone(String number) {
        phones.add(new Phone(number));
    }

    public void removePhone(String number) {
        phones.removeIf(it -> it.getNumber().equals(number));
    }

}
