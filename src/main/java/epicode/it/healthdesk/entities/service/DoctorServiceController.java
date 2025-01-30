package epicode.it.healthdesk.entities.service;

import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorResponse;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class DoctorServiceController {
    private final DoctorSvc doctorSvc;
    private final DoctorMapper mapper;

    @PutMapping("/{id}/change-availability")
    public ResponseEntity<DoctorResponse> changeAvailability(@PathVariable Long id, @RequestParam Long serviceId) {
        return ResponseEntity.ok(mapper.fromDoctorToDoctorResponse(doctorSvc.updateServiceAvailability(id, serviceId)));
    }

    @PutMapping("/{id}/change-activation")
    public ResponseEntity<DoctorResponse> changeActivation(@PathVariable Long id, @RequestParam Long serviceId) {
        return ResponseEntity.ok(mapper.fromDoctorToDoctorResponse(doctorSvc.updateServiceActivation(id, serviceId)));
    }

    @DeleteMapping("/{id}/delete-service")
    public ResponseEntity<DoctorResponse> deleteService(@PathVariable Long id, @RequestParam Long serviceId) {
        return ResponseEntity.ok(mapper.fromDoctorToDoctorResponse(doctorSvc.deleteService(id, serviceId)));
    }
}
