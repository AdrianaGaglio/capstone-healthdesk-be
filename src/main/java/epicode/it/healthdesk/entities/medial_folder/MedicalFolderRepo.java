package epicode.it.healthdesk.entities.medial_folder;

import epicode.it.healthdesk.entities.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalFolderRepo extends JpaRepository<MedicalFolder, Long> {
    public MedicalFolder findFirstByPatient(Patient patient);
}
