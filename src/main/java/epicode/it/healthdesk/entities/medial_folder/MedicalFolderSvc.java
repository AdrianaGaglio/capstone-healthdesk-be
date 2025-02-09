package epicode.it.healthdesk.entities.medial_folder;

import epicode.it.healthdesk.entities.document.certificate.Certificate;
import epicode.it.healthdesk.entities.document.certificate.CertificateSvc;
import epicode.it.healthdesk.entities.document.document.DocumentCreateRequest;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.document.prescription.Prescription;
import epicode.it.healthdesk.entities.document.prescription.PrescriptionSvc;
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

    public MedicalFolder addPrescription(Long id, DocumentCreateRequest request) {
        MedicalFolder mf = getById(id);
        mf.getPrescriptions().add(prescriptionSvc.create(mf, request));
        return medicalFolderRepo.save(mf);
    }

    public MedicalFolder addCertificate(Long id, DocumentCreateRequest request) {
        MedicalFolder mf = getById(id);
        mf.getDocumentation().add(certificateSvc.create(mf, request));
        return medicalFolderRepo.save(mf);
    }

    @Transactional
    public MedicalFolder deletePrescription(Long id, Long prescriptionId) {
        MedicalFolder mf = getById(id);
        Prescription p = prescriptionSvc.getById(prescriptionId);
        mf.getPrescriptions().remove(p);
        prescriptionSvc.delete(prescriptionId);
        return medicalFolderRepo.save(mf);
    }

    @Transactional
    public MedicalFolder deleteCertificate(Long id, Long certificateId) {
        MedicalFolder mf = getById(id);
        Certificate c = certificateSvc.getById(certificateId);
        mf.getDocumentation().remove(c);
        certificateSvc.delete(certificateId);
        return medicalFolderRepo.save(mf);
    }


}
