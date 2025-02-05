package epicode.it.healthdesk.entities.prescription;

import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionSvc {
    private final PrescriptionRepo prescriptionRepo;

    public List<Prescription> getAll() {

        return prescriptionRepo.findAll();
    }

    public Page<Prescription> getAllPageable(Pageable pageable) {
        return prescriptionRepo.findAll(pageable);
    }

    public Prescription getById(Long id) {
        return prescriptionRepo.findById(id).orElse(null);
    }

    public int count() {
        return (int) prescriptionRepo.count();
    }

    public String delete(Long id) {
        Prescription e = getById(id);
        prescriptionRepo.delete(e);
        return "Prescription deleted successfully";
    }

    public String delete(Prescription e) {
        Prescription foundPrescription = getById(e.getId());
        prescriptionRepo.delete(foundPrescription);
        return "Prescription deleted successfully";
    }

    @Transactional
    public Prescription create(MedicalFolder mf, String file) {
        Prescription p = new Prescription();
        p.setFile(file);
        p.setMedicalFolder(mf);
        return prescriptionRepo.save(p);
    }
}
