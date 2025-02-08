package epicode.it.healthdesk.entities.patient;

import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.patient.dto.PatientMapper;
import epicode.it.healthdesk.entities.patient.dto.PatientResponse;
import epicode.it.healthdesk.entities.patient.dto.PatientUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PatientController {
    private final PatientSvc patientSvc;
    private final DoctorSvc doctorSvc;
    private final PatientMapper mapper;

    @GetMapping
    public ResponseEntity<PatientResponse> getPatient(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(mapper.fromPatientToPatientResponse(patientSvc.getByEmail(userDetails.getUsername())));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PatientResponse>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        List<Patient> patients = patientSvc.getAll();

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            patients = patients.stream().filter(p -> d.getPatients().contains(p)).toList();
        }

        return ResponseEntity.ok(mapper.fromPatientToPatientResponseList(patients));
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Page<PatientResponse>> getAllPaged(@ParameterObject Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            return ResponseEntity.ok(mapper.fromPatientPagedToPatientResponsePaged(patientSvc.getAllPageableByDoctor(d.getId(), pageable)));
        }

        return ResponseEntity.ok(mapper.fromPatientPagedToPatientResponsePaged(patientSvc.getAllPageable(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            Patient p = patientSvc.getByEmail(userDetails.getUsername());

            if (!p.getId().equals(id)) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return ResponseEntity.ok(mapper.fromPatientToPatientResponse(patientSvc.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            Patient p = patientSvc.getByEmail(userDetails.getUsername());

            if (!p.getId().equals(id)) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", patientSvc.delete(id));
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> update(@PathVariable Long id, @RequestBody PatientUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        Patient p = patientSvc.getByEmail(userDetails.getUsername());
        if (p.getId() != id) throw new AccessDeniedException("Accesso negato");

        return ResponseEntity.ok(mapper.fromPatientToPatientResponse(patientSvc.update(id, request)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Page<PatientResponse>> findByNameOrSurname(@RequestParam String identifier, @ParameterObject Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {

        Page<Patient> patients = patientSvc.findByNameOrSurname(identifier, pageable);

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            return ResponseEntity.ok(mapper.fromPatientPagedToPatientResponsePaged(patientSvc.findByDoctorAndNameOrSurname(d.getId(), identifier, pageable)));
        }

        return ResponseEntity.ok(mapper.fromPatientPagedToPatientResponsePaged(patients));
    }

}
