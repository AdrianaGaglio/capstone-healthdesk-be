package epicode.it.healthdesk.entities.specialization;

import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="specializations")
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

}