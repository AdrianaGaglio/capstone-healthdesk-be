package epicode.it.healthdesk.entities.service;

import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorResponse;
import epicode.it.healthdesk.entities.service.dto.DoctorServiceMapper;
import epicode.it.healthdesk.entities.service.dto.DoctorServiceResponse;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
public class DoctorServiceController {
    private final DoctorSvc doctorSvc;
    private final DoctorServiceSvc serviceSvc;
    private final DoctorServiceMapper svcMapper;
    private final DoctorMapper mapper;

    @PutMapping("/{id}")
    public ResponseEntity<DoctorServiceResponse> update(@PathVariable Long id, @RequestBody DoctorService request) {
        return new ResponseEntity<>(svcMapper.toDoctorServiceResponse(serviceSvc.update(id, request)), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}/change-availability")
    public ResponseEntity<DoctorResponse> changeAvailability(@PathVariable Long id, @RequestParam Long serviceId) {
        return new ResponseEntity<>(mapper.fromDoctorToDoctorResponse(doctorSvc.updateServiceAvailability(id, serviceId)), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}/change-activation")
    public ResponseEntity<DoctorResponse> changeActivation(@PathVariable Long id, @RequestParam Long serviceId) {
        return new ResponseEntity<>(mapper.fromDoctorToDoctorResponse(doctorSvc.updateServiceActivation(id, serviceId)), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}/delete-service")
    public ResponseEntity<DoctorResponse> deleteService(@PathVariable Long id, @RequestParam Long serviceId) {
        return new ResponseEntity<>(mapper.fromDoctorToDoctorResponse(doctorSvc.deleteService(id, serviceId)), HttpStatus.ACCEPTED);
    }


}
