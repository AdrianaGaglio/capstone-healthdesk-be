package epicode.it.healthdesk.entities.addresses.province;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="provinces")
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String code;

    private String acronym;

    private String region;

}