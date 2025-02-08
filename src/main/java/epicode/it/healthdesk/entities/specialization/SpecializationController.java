package epicode.it.healthdesk.entities.specialization;

import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/specialization")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
public class SpecializationController {
    private final DoctorSvc doctorSvc;
    private final DoctorMapper mapper;

    @DeleteMapping("/{id}/delete-specialization")
    public ResponseEntity<DoctorResponse> deleteSpecialization(@PathVariable Long id, @RequestParam Long specializationId) {
        return ResponseEntity.ok(mapper.fromDoctorToDoctorResponse(doctorSvc.deleteSpecialization(id, specializationId)));
    }
}