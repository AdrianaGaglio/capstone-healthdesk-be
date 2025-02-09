package epicode.it.healthdesk.entities.document.document;

import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name ="documents")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String file;

    private LocalDate date;

    private String description;

    @ManyToOne
    @JoinColumn(name = "medical_folder_id")
    private MedicalFolder medicalFolder;

}