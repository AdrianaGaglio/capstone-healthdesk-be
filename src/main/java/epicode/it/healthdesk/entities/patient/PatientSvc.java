package epicode.it.healthdesk.entities.patient;

import epicode.it.healthdesk.entities.address.AddressSvc;
import epicode.it.healthdesk.entities.medial_folder.MedicalFolderSvc;
import epicode.it.healthdesk.entities.patient.dto.PatientMapper;
import epicode.it.healthdesk.entities.patient.dto.PatientRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class PatientSvc {
    private final PatientRepo patientRepo;
    private final AddressSvc addressSvc;
    private final MedicalFolderSvc medicalFolderSvc;
    private final PatientMapper mapper;

    public List<Patient> getAll() {
        return patientRepo.findAll();
    }

    public Page<Patient> getAllPageable(Pageable pageable) {
        return patientRepo.findAll(pageable);
    }

    public Patient getById(Long id) {
        return patientRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Paziente non trovato"));
    }

    public int count() {
        return (int) patientRepo.count();
    }

    public String delete(Long id) {
        Patient e = getById(id);
        patientRepo.delete(e);
        return "Paziente cancellato correttamente";
    }

    public String delete(Patient e) {
        Patient foundPatient = getById(e.getId());
        patientRepo.delete(foundPatient);
        return "Paziente cancellato correttamente";
    }

    public Patient findByTaxId(String taxId) {
        return patientRepo.findByTaxId(taxId).orElseThrow(() -> new EntityNotFoundException("Paziente non trovato"));
    }

    public boolean existsByTaxId(String taxId) {
        return patientRepo.existsByTaxId(taxId);
    }

    @Transactional
    public Patient create(@Valid PatientRequest request) {
        if (existsByTaxId(request.getTaxId())) {
            throw new EntityExistsException("Codice fiscale già registrato");
        }

        Patient p = mapper.fromPatientRequestToPatient(request);
        p.setAddress(addressSvc.create(request.getAddress()));

        medicalFolderSvc.create(p);

        return patientRepo.save(p);
    }
}
