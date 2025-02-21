package epicode.it.healthdesk.entities.patient;

import com.google.api.Http;
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

    @GetMapping // per il paziente
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> getPatient(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(mapper.fromPatientToPatientResponse(patientSvc.getByEmail(userDetails.getUsername())), HttpStatus.OK);
    }

    @GetMapping("/all") // per admin e medico
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PatientResponse>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        List<Patient> patients = patientSvc.getAll();

        // il medico può visualizzare solo i pazienti che hanno prenotato un appuntamento con lui
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            patients = patients.stream().filter(p -> d.getPatients().contains(p)).toList();
        }

        return new ResponseEntity<>(mapper.fromPatientToPatientResponseList(patients), HttpStatus.OK);
    }

    @GetMapping("/paged") // per admin e medico
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Page<PatientResponse>> getAllPaged(@ParameterObject Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {

        // il medico visualizza solo i pazienti che hanno prenotato un appuntamento con lui
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            return ResponseEntity.ok(mapper.fromPatientPagedToPatientResponsePaged(patientSvc.getAllPageableByDoctor(d.getId(), pageable)));
        }

        return new ResponseEntity<>(mapper.fromPatientPagedToPatientResponsePaged(patientSvc.getAllPageable(pageable)), HttpStatus.OK);
    }

    @GetMapping("/{id}") // per il paziente
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> getById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il paziente corrisponde all'id
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            Patient p = patientSvc.getByEmail(userDetails.getUsername());

            if (!p.getId().equals(id)) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return new ResponseEntity<>(mapper.fromPatientToPatientResponse(patientSvc.getById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}") //❗ implementare tutte le logiche che ripuliscono le info sul paziente in caso di cancellazione del profilo
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il paziente sta tentando di cancellare il proprio profilo
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

        // controlla se il paziente corrisponde all'id
        Patient p = patientSvc.getByEmail(userDetails.getUsername());
        if (p.getId() != id) throw new AccessDeniedException("Accesso negato");

        return new ResponseEntity<>(mapper.fromPatientToPatientResponse(patientSvc.update(id, request)), HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Page<PatientResponse>> findByNameOrSurname(@RequestParam String identifier, @ParameterObject Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {

        Page<Patient> patients = patientSvc.findByNameOrSurname(identifier, pageable);

        // il medico visualizza solo i pazienti che hanno prenotato appuntamenti con lui
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            return ResponseEntity.ok(mapper.fromPatientPagedToPatientResponsePaged(patientSvc.findByDoctorAndNameOrSurname(d.getId(), identifier, pageable)));
        }

        return new ResponseEntity<>(mapper.fromPatientPagedToPatientResponsePaged(patients), HttpStatus.OK);
    }

}
