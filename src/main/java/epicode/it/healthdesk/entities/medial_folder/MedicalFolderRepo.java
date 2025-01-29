package epicode.it.healthdesk.entities.medial_folder;

import epicode.it.healthdesk.entities.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedicalFolderRepo extends JpaRepository<MedicalFolder, Long> {
    @Query("SELECT mf From MedicalFolder mf WHERE mf.patient.id = :patientId")
    public MedicalFolder findFirstByPatientId(@Param("patientId") Long patientId);
}
