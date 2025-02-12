package epicode.it.healthdesk.entities.document.certificate;

import epicode.it.healthdesk.entities.document.document.DocumentCreateRequest;
import epicode.it.healthdesk.entities.document.prescription.Prescription;
import epicode.it.healthdesk.utilities.email.EmailMapper;
import epicode.it.healthdesk.utilities.email.EmailRequest;
import epicode.it.healthdesk.utilities.email.EmailSvc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import jakarta.transaction.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Validated
public class CertificateSvc {
    private final CertificateRepo certificateRepo;
    private final EmailMapper emailMapper;
    private final EmailSvc emailSvc;

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


    // registro un nuovo certificato nel db
    @Transactional
    public Certificate create(MedicalFolder mf, @Valid DocumentCreateRequest request) {
        Certificate c = new Certificate();
        BeanUtils.copyProperties(request, c);
        c.setDate(LocalDate.now());
        c.setMedicalFolder(mf); // lo assegno alla cartella medica
        c=certificateRepo.save(c);

        EmailRequest mail = new EmailRequest();
        mail.setTo("infohealthdesk@gmail.com");
        mail.setSubject("Health Desk - Nuovo documento disponibile");
        mail.setBody(emailMapper.toNewDocument(mf.getPatient()));
        emailSvc.sendEmailWithAttachment(mail, c.getFile());

        return c;
    }
}
