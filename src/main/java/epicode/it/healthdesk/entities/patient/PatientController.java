package epicode.it.healthdesk.entities.patient;

import epicode.it.healthdesk.entities.patient.dto.PatientMapper;
import epicode.it.healthdesk.entities.patient.dto.PatientRequest;
import epicode.it.healthdesk.entities.patient.dto.PatientResponse;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final PatientMapper mapper;

    @GetMapping
    public ResponseEntity<PatientResponse> getPatient(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(mapper.fromPatientToPatientResponse(patientSvc.getByEmail(userDetails.getUsername())));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PatientResponse>> getAll() {

        return ResponseEntity.ok(mapper.fromPatientToPatientResponseList(patientSvc.getAll()));
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Page<PatientResponse>> getAllPaged(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(mapper.fromPatientPagedToPatientResponsePaged(patientSvc.getAllPageable(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            System.out.println("paziente");
        }

        return ResponseEntity.ok(mapper.fromPatientToPatientResponse(patientSvc.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            System.out.println("paziente");
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", patientSvc.delete(id));
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }


}
