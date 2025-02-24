package epicode.it.healthdesk.entities.training;

import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOCTOR')")
public class TrainingController {
    private final DoctorSvc doctorSvc;
    private final DoctorMapper mapper;

    // rimuove corso di formazione dal cv del medico e ritorna il medico aggiornato
    @DeleteMapping("/{id}/delete-training")
    public ResponseEntity<DoctorResponse> deleteTraining(@PathVariable Long id, @RequestParam Long trainingId) {
        return ResponseEntity.ok(mapper.fromDoctorToDoctorResponse(doctorSvc.deleteTraining(id, trainingId)));
    }
}
