package epicode.it.healthdesk.entities.document.certificate;

import epicode.it.healthdesk.entities.document.prescription.Prescription;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;



import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import jakarta.transaction.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CertificateSvc {
    private final CertificateRepo certificateRepo;

    public List<Certificate> getAll() {

        return certificateRepo.findAll();
    }

    public Page<Certificate> getAllPageable(Pageable pageable) {
        return certificateRepo.findAll(pageable);
    }

    public Certificate getById(Long id) {
        return certificateRepo.findById(id).orElse(null);
    }

    public int count() {
        return (int) certificateRepo.count();
    }

    public String delete(Long id) {
        Certificate e = getById(id);
        certificateRepo.delete(e);
        return "Documento eliminato con successo";
    }

    public String delete(Prescription e) {
        Certificate foundPrescription = getById(e.getId());
        certificateRepo.delete(foundPrescription);
        return "Documento eliminato con successo";
    }

    @Transactional
    public Certificate create(MedicalFolder mf, String file) {
        Certificate p = new Certificate();
        p.setFile(file);
        p.setDate(LocalDate.now());
        p.setMedicalFolder(mf);
        return certificateRepo.save(p);
    }
}
