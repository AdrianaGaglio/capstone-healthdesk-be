package epicode.it.healthdesk.entities.medial_folder;

import epicode.it.healthdesk.entities.patient.Patient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalFolderSvc {
    private final MedicalFolderRepo medicalFolderRepo;

    public List<MedicalFolder> getAll() {
        return medicalFolderRepo.findAll();
    }

    public Page<MedicalFolder> getAllPageable(Pageable pageable) {
        return medicalFolderRepo.findAll(pageable);
    }

    public MedicalFolder getById(Long id) {
        return medicalFolderRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Cartella medica non trovata"));
    }

    public int count() {
        return (int) medicalFolderRepo.count();
    }

    public String delete(Long id) {
        MedicalFolder e = getById(id);
        medicalFolderRepo.delete(e);
        return "Cartella medica eliminata correttamente";
    }

    public String delete(MedicalFolder e) {
        MedicalFolder foundMedicalFolder = getById(e.getId());
        medicalFolderRepo.delete(foundMedicalFolder);
        return "Cartella medica eliminata correttamente";
    }

    public MedicalFolder create(Patient p) {
        MedicalFolder mf = new MedicalFolder();
        mf.setPatient(p);
        return medicalFolderRepo.save(mf);
    }

    public MedicalFolder getByPatient(Long patientId) {
        return medicalFolderRepo.findFirstByPatientId(patientId);
    }
}
