package epicode.it.healthdesk.entities.doctor;

import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorResponse;
import epicode.it.healthdesk.entities.doctor.dto.DoctorUpdateAddInfoRequest;
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
@PreAuthorize("isAuthenticated()")
public class DoctorController {
    private final DoctorSvc doctorSvc;
    private final DoctorMapper mapper;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DoctorResponse>> getAll() {
        return ResponseEntity.ok(mapper.fromDoctorToDoctorResponseList(doctorSvc.getAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.fromDoctorToDoctorResponse(doctorSvc.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponse> getAll(@AuthenticationPrincipal UserDetails userDetails) {

        Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(mapper.fromDoctorToDoctorResponse(doctorSvc.getById(d.getId())));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        response.put("message", doctorSvc.delete(id));
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<DoctorResponse> updateAddInfo(@PathVariable Long id, @RequestBody DoctorUpdateAddInfoRequest request) {
        return ResponseEntity.ok(mapper.fromDoctorToDoctorResponse(doctorSvc.updateAddInfo(id, request)));
    }
}
