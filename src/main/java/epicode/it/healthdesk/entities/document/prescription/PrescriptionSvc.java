package epicode.it.healthdesk.entities.document.prescription;

import epicode.it.healthdesk.entities.document.document.DocumentCreateRequest;
import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import epicode.it.healthdesk.utilities.email.EmailMapper;
import epicode.it.healthdesk.utilities.email.EmailRequest;
import epicode.it.healthdesk.utilities.email.EmailSvc;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class PrescriptionSvc {
    private final PrescriptionRepo prescriptionRepo;
    private final EmailMapper emailMapper;
    private final EmailSvc emailSvc;

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
        return "Prescrizione eliminata con successo";
    }

    public String delete(Prescription e) {
        Prescription foundPrescription = getById(e.getId());
        prescriptionRepo.delete(foundPrescription);
        return "Prescrizione eliminata con successo";
    }

    // registro una nuova prescrizione nel db
    @Transactional
    public Prescription create(MedicalFolder mf, @Valid DocumentCreateRequest request) {
        Prescription p = new Prescription();
        BeanUtils.copyProperties(request, p);
        p.setDate(LocalDate.now());
        p.setMedicalFolder(mf); // lo assegno alla cartella medica


        p = prescriptionRepo.save(p);

        EmailRequest mail = new EmailRequest();
        mail.setTo(mf.getPatient().getAppUser().getEmail());
        mail.setSubject("Health Desk - Nuova prescrizione disponibile");
        mail.setBody(emailMapper.toNewPrescription(mf.getPatient()));
        emailSvc.sendEmailWithAttachment(mail, p.getFile());

        return p;
    }
}
