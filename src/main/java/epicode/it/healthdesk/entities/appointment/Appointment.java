package epicode.it.healthdesk.entities.appointment;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


}