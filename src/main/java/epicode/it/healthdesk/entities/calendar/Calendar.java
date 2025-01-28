package epicode.it.healthdesk.entities.calendar;

import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "calendars")
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

}