package epicode.it.healthdesk.entities.experience;

import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/experience")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
public class ExperienceController {
    private final DoctorSvc doctorSvc;
    private final DoctorMapper mapper;

    @DeleteMapping("/{id}/delete-experience")
    public ResponseEntity<DoctorResponse> deleteExperience(@PathVariable Long id, @RequestParam Long experienceId) {
        return ResponseEntity.ok(mapper.fromDoctorToDoctorResponse(doctorSvc.deleteExperience(id, experienceId)));
    }
}
