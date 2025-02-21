package epicode.it.healthdesk.entities.doctor;

import epicode.it.healthdesk.auth.dto.RegisterDoctorRequest;
import epicode.it.healthdesk.entities.doctor.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorSvc doctorSvc;
    private final DoctorMapper mapper;

    @GetMapping("/all") // solo per admin
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DoctorResponse>> getAll() {
        return new ResponseEntity<>(mapper.fromDoctorToDoctorResponseList(doctorSvc.getAll()), HttpStatus.OK);
    }

    @GetMapping("/{id}") // per pazienti e admin
    public ResponseEntity<DoctorResponse> getById(@PathVariable Long id) {
        return new ResponseEntity<>(mapper.fromDoctorToDoctorResponse(doctorSvc.getById(id)), HttpStatus.OK);
    }

    @GetMapping // per il medico
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponse> getDoctor(@AuthenticationPrincipal UserDetails userDetails) {

        Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
        return new ResponseEntity<>(mapper.fromDoctorToDoctorResponse(doctorSvc.getById(d.getId())), HttpStatus.OK);

    }

    @DeleteMapping("/{id}") // solo per l'admin
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        response.put("message", doctorSvc.delete(id));
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}") // per admin e medico
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<DoctorResponse> updateAddInfo(@PathVariable Long id, @RequestBody DoctorUpdateAddInfoRequest request) {
        return new ResponseEntity<>(mapper.fromDoctorToDoctorResponse(doctorSvc.updateAddInfo(id, request)), HttpStatus.ACCEPTED);
    }

    @PutMapping("/update-personal-info/{id}") // per il medico
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponse> updatePersonalInfo(@PathVariable Long id, @RequestBody DoctorUpdateRequest request) {
        return new ResponseEntity<>(mapper.fromDoctorToDoctorResponse(doctorSvc.updatePersonalInfo(id, request)), HttpStatus.ACCEPTED);
    }

}
