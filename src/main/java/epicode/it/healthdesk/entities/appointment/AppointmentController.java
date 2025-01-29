package epicode.it.healthdesk.entities.appointment;

import epicode.it.healthdesk.entities.appointment.dto.AppointmentMapper;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentRequest;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentSvc appointmentSvc;
    private final AppointmentMapper mapper;

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody AppointmentRequest request) {
        return new ResponseEntity<>(mapper.toAppointmentResponse(appointmentSvc.create(request)), HttpStatus.CREATED);
    }
}
