package epicode.it.healthdesk.entities.medial_folder;

import epicode.it.healthdesk.entities.document.certificate.Certificate;
import epicode.it.healthdesk.entities.document.certificate.CertificateSvc;
import epicode.it.healthdesk.entities.document.document.DocumentCreateRequest;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.document.prescription.Prescription;
import epicode.it.healthdesk.entities.document.prescription.PrescriptionSvc;
import epicode.it.healthdesk.entities.reminder.Reminder;
import epicode.it.healthdesk.entities.reminder.ReminderSvc;
import epicode.it.healthdesk.entities.reminder.dto.ReminderRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalFolderSvc {
    private final MedicalFolderRepo medicalFolderRepo;
    private final PrescriptionSvc prescriptionSvc;
    private final CertificateSvc certificateSvc;
    private final ReminderSvc reminderSvc;

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

    // creo cartella medica, viene eseguito contestualmente alla creazione di un paziente
    public MedicalFolder create(Patient p) {
        MedicalFolder mf = new MedicalFolder();
        mf.setPatient(p);
        return medicalFolderRepo.save(mf);
    }

    // ottengo la cartella medica per paziente
    public MedicalFolder getByPatient(Long patientId) {
        return medicalFolderRepo.findFirstByPatientId(patientId);
    }

    // ritorno la cartella medica aggiornata
    // dopo l'inserimento di una prescrizione
    public MedicalFolder addPrescription(Long id, DocumentCreateRequest request) {
        MedicalFolder mf = getById(id);
        mf.getPrescriptions().add(prescriptionSvc.create(mf, request));
        return medicalFolderRepo.save(mf);
    }

    // ritorno la cartella medica aggiornata
    // dopo l'inserimento di un nuovo certificato
    public MedicalFolder addCertificate(Long id, DocumentCreateRequest request) {
        MedicalFolder mf = getById(id);
        mf.getDocumentation().add(certificateSvc.create(mf, request));
        return medicalFolderRepo.save(mf);
    }

    // ritorno la cartella medica aggiornata
    // dopo la cancellazione di una prescrizione
    @Transactional
    public MedicalFolder deletePrescription(Long id, Long prescriptionId) {
        MedicalFolder mf = getById(id);
        Prescription p = prescriptionSvc.getById(prescriptionId);
        mf.getPrescriptions().remove(p);
        prescriptionSvc.delete(prescriptionId);
        return medicalFolderRepo.save(mf);
    }

    // ritorno la cartella medica aggiornata
    // dopo la cancellazione di un certificato
    @Transactional
    public MedicalFolder deleteCertificate(Long id, Long certificateId) {
        MedicalFolder mf = getById(id);
        Certificate c = certificateSvc.getById(certificateId);
        mf.getDocumentation().remove(c);
        certificateSvc.delete(certificateId);
        return medicalFolderRepo.save(mf);
    }

    // ritorno cartella medica dopo aggiunta reminder
    @Transactional
    public MedicalFolder addReminder(Long id, ReminderRequest request) {
        MedicalFolder mf = getById(id);
        mf.getReminders().add(reminderSvc.create(mf, request));
        return medicalFolderRepo.save(mf);
    }

    @Transactional
    public MedicalFolder removeReminder(Long id, Long reminderId) {
        MedicalFolder mf = getById(id);
        Reminder r = reminderSvc.getById(reminderId);
        mf.getReminders().remove(r);
        reminderSvc.delete(r);
        return medicalFolderRepo.save(mf);
    }


}
