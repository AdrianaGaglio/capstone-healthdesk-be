package epicode.it.healthdesk.entities.appointment;

import epicode.it.healthdesk.entities.appointment.dto.AppointmentMapper;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentRequest;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentResponse;
import epicode.it.healthdesk.entities.calendar.dto.CalendarMapper;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.exceptions.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentSvc appointmentSvc;
    private final DoctorSvc doctorSvc;
    private final AppointmentMapper mapper;
    private final CalendarMapper calendarMapper;

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody AppointmentRequest request) {
        return new ResponseEntity<>(mapper.toAppointmentResponse(appointmentSvc.create(request)), HttpStatus.CREATED);
    }

    @GetMapping("/next/{calendarId}")
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public ResponseEntity<?> findNextByCalendar(@PathVariable Long calendarId, @ParameterObject Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {

        Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
        if (!d.getCalendar().getId().equals(calendarId)) {
            ErrorMessage err = new ErrorMessage("Accesso negato", HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(mapper.toAppointmentsPaged(appointmentSvc.findByCalendarNext(calendarId, pageable)), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Appointment request, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            if (!d.getCalendar().getId().equals(request.getCalendar().getId())) {
                ErrorMessage err = new ErrorMessage("Accesso negato", HttpStatus.FORBIDDEN);
                return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(calendarMapper.toCalendarResponse((appointmentSvc.update(id, request))), HttpStatus.OK);
    }
}
