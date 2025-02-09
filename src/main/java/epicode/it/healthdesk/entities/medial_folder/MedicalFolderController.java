package epicode.it.healthdesk.entities.medial_folder;

import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.medial_folder.dto.MedicalFolderMapper;
import epicode.it.healthdesk.entities.medial_folder.dto.MedicalFolderResponse;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.patient.PatientSvc;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicalFolderResponse> get(@AuthenticationPrincipal UserDetails userDetails) {
        Patient p = patientSvc.getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(mapper.toMedicalFolderResponse(medicalFolderSvc.getByPatient(p.getId())));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicalFolderResponse> getByPatient(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toMedicalFolderResponse(medicalFolderSvc.getByPatient(id)));
    }

    @PutMapping("/{id}/add-prescription")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicalFolderResponse> addPrescription(@PathVariable Long id, @RequestParam String file, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            MedicalFolder mf = medicalFolderSvc.getById(id);
            if (!d.getPatients().contains(mf.getPatient())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return ResponseEntity.ok(mapper.toMedicalFolderResponse(medicalFolderSvc.addPrescription(id, file)));
    }

    @PutMapping("/{id}/add-certificate")
    @PreAuthorize("hasAnyRole('PATIENT')")
    public ResponseEntity<MedicalFolderResponse> addCertificate(@PathVariable Long id, @RequestParam String file, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            MedicalFolder mf = medicalFolderSvc.getById(id);
            Patient p = patientSvc.getByEmail(userDetails.getUsername());
            if (mf.getPatient().getId() != p.getId()) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return ResponseEntity.ok(mapper.toMedicalFolderResponse(medicalFolderSvc.addCertificate(id, file)));
    }

    @DeleteMapping("/{id}/delete-prescription")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicalFolderResponse> deletePrescription(@PathVariable Long id, @RequestParam Long prescriptionId, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            MedicalFolder mf = medicalFolderSvc.getById(id);
            if (!d.getPatients().contains(mf.getPatient())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return ResponseEntity.ok(mapper.toMedicalFolderResponse(medicalFolderSvc.deletePrescription(id, prescriptionId)));
    }
}
