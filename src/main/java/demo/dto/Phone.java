package demo.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Audited
public class Phone {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String number;

}
