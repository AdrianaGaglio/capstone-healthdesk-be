package epicode.it.healthdesk.entities.training;

import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false, length = 1000)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    private Doctor doctor;
}