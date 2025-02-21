package epicode.it.healthdesk.entities.medial_folder;

import com.google.api.Http;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.document.document.Document;
import epicode.it.healthdesk.entities.document.document.DocumentCreateRequest;
import epicode.it.healthdesk.entities.medial_folder.dto.MedicalFolderMapper;
import epicode.it.healthdesk.entities.medial_folder.dto.MedicalFolderResponse;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.patient.PatientSvc;
import epicode.it.healthdesk.entities.reminder.dto.ReminderRequest;
import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-folder")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MedicalFolderController {
    private final MedicalFolderSvc medicalFolderSvc;
    private final DoctorSvc doctorSvc;
    private final MedicalFolderMapper mapper;
    private final PatientSvc patientSvc;

    @GetMapping // per il paziente
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicalFolderResponse> get(@AuthenticationPrincipal UserDetails userDetails) {
        Patient p = patientSvc.getByEmail(userDetails.getUsername());
        return new ResponseEntity<>(mapper.toMedicalFolderResponse(medicalFolderSvc.getByPatient(p.getId())), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalFolderResponse> getByPatient(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il medico segue il paziente
        Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
        if (!d.getPatients().stream().anyMatch(p -> p.getId().equals(id))) {
            throw new AccessDeniedException("Accesso negato");
        }

        return new ResponseEntity<>(mapper.toMedicalFolderResponse(medicalFolderSvc.getByPatient(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}/add-prescription") // il medico carica prescrizioni
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalFolderResponse> addPrescription(@PathVariable Long id, @RequestBody DocumentCreateRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il medico segue il paziente
        Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
        MedicalFolder mf = medicalFolderSvc.getById(id);
        if (!d.getPatients().contains(mf.getPatient())) {
            throw new AccessDeniedException("Accesso negato");
        }

        return new ResponseEntity<>(mapper.toMedicalFolderResponse(medicalFolderSvc.addPrescription(id, request)), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}/add-certificate") // il paziente carica referti
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicalFolderResponse> addCertificate(@PathVariable Long id, @RequestBody DocumentCreateRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il paziente è proprietario di quella cartella medica
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            MedicalFolder mf = medicalFolderSvc.getById(id);
            Patient p = patientSvc.getByEmail(userDetails.getUsername());
            if (mf.getPatient().getId() != p.getId()) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return new ResponseEntity<>(mapper.toMedicalFolderResponse(medicalFolderSvc.addCertificate(id, request)), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}/delete-prescription") // il medico cancella prescrizioni
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalFolderResponse> deletePrescription(@PathVariable Long id, @RequestParam Long prescriptionId, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il medico segue il paziente
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            MedicalFolder mf = medicalFolderSvc.getById(id);
            if (!d.getPatients().contains(mf.getPatient())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return new ResponseEntity<>(mapper.toMedicalFolderResponse(medicalFolderSvc.deletePrescription(id, prescriptionId)), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}/delete-certificate") // il paziente cancella il referto caricato
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicalFolderResponse> deleteCertificate(@PathVariable Long id, @RequestParam Long certificateId, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il paziente è proprietario della cartella medica
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            Patient p = patientSvc.getByEmail(userDetails.getUsername());
            MedicalFolder mf = medicalFolderSvc.getById(id);
            if (!p.getId().equals(mf.getPatient().getId())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return new ResponseEntity<>(mapper.toMedicalFolderResponse(medicalFolderSvc.deleteCertificate(id, certificateId)), HttpStatus.ACCEPTED);
    }

    @PostMapping("/{id}/add-reminder") // per il medico
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalFolderResponse> addReminder(@PathVariable Long id, @RequestBody ReminderRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il paziente è seguito dal medico
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            MedicalFolder mf = medicalFolderSvc.getById(id);
            if (!d.getPatients().contains(mf.getPatient())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }
        return new ResponseEntity<>(mapper.toMedicalFolderResponse(medicalFolderSvc.addReminder(id, request)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/remove-reminder") // per il medico
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalFolderResponse> removeReminder(@PathVariable Long id, @RequestParam Long reminderId, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il medico segue il paziente
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            MedicalFolder mf = medicalFolderSvc.getById(id);
            if (!d.getPatients().contains(mf.getPatient())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }
        return new ResponseEntity<>(mapper.toMedicalFolderResponse(medicalFolderSvc.removeReminder(id, reminderId)), HttpStatus.ACCEPTED);
    }

}
