package epicode.it.healthdesk.entities.medial_folder;

import epicode.it.healthdesk.entities.medial_folder.dto.MedicalFolderMapper;
import epicode.it.healthdesk.entities.medial_folder.dto.MedicalFolderResponse;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.patient.PatientSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medical-folder")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MedicalFolderController {
    private final MedicalFolderSvc medicalFolderSvc;
    private final MedicalFolderMapper mapper;
    private final PatientSvc patientSvc;

    @GetMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicalFolderResponse> get(@AuthenticationPrincipal UserDetails userDetails) {
        Patient p = patientSvc.getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(mapper.toMedicalFolderResponse(medicalFolderSvc.getByPatient(p.getId())));
    }

}
