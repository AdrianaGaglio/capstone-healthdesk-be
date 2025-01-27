package epicode.it.healthdesk.entities.prescription;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


}