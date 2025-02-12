package epicode.it.healthdesk.entities.reminder;

import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Frequency frequency;

    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    private Boolean isActive;

    @ManyToOne
    private MedicalFolder medicalFolder;

    private LocalDate lastSent;
}