package epicode.it.healthdesk.entities.specialization;

import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "specializations")
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String name;

    @Column(length = 1000)
    private String description;

    private LocalDate date;

    @ManyToOne
    private Doctor doctor;

}