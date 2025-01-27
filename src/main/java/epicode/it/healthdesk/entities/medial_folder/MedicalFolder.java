package epicode.it.healthdesk.entities.medial_folder;

import epicode.it.healthdesk.entities.patient.Patient;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "medical_folders")
public class MedicalFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    private Patient patient;

}