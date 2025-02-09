package epicode.it.healthdesk.entities.appointment;

import epicode.it.healthdesk.entities.appointment.dto.*;
import epicode.it.healthdesk.entities.calendar.dto.CalendarMapper;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.patient.PatientSvc;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
    private final PatientSvc patientSvc;
    private final AppointmentMapper mapper;
    private final CalendarMapper calendarMapper;

    // prenotazione appuntamento
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody AppointmentRequest request) {
        return new ResponseEntity<>(mapper.toAppointmentResponse(appointmentSvc.create(request)), HttpStatus.CREATED);
    }

    // prossimi appuntamenti medico
    @GetMapping("/next/{calendarId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<?> findNextByCalendar(@PathVariable Long calendarId, @ParameterObject Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            if (!d.getCalendar().getId().equals(calendarId)) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return new ResponseEntity<>(mapper.toAppointmentsPaged(appointmentSvc.findByCalendarNext(calendarId, pageable)), HttpStatus.OK);

    }

    // modifica appuntamento medico
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Appointment request, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            Appointment a = appointmentSvc.getById(id);
            if (!d.getCalendar().getId().equals(a.getCalendar().getId())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }
        return new ResponseEntity<>(calendarMapper.toCalendarResponse((appointmentSvc.update(id, request))), HttpStatus.OK);
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponseForMedicalFolder> cancel(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        Patient p = patientSvc.getByEmail(userDetails.getUsername());
        Appointment a = appointmentSvc.getById(id);
        if (!p.getId().equals(a.getMedicalFolder().getPatient().getId())) {
            throw new AccessDeniedException("Accesso negato");
        }

        return new ResponseEntity<>(mapper.toAppResponseForMF(appointmentSvc.cancelApp(id)), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponseForMedicalFolder> updateDate(@PathVariable Long id, @RequestBody AppointmentDateUpdate request, @AuthenticationPrincipal UserDetails userDetails) {

        Patient p = patientSvc.getByEmail(userDetails.getUsername());
        Appointment a = appointmentSvc.getById(id);
        if (!p.getId().equals(a.getMedicalFolder().getPatient().getId())) {
            throw new AccessDeniedException("Accesso negato");
        }

        return ResponseEntity.ok(mapper.toAppResponseForMF(appointmentSvc.updateDate(id, request)));

    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppointmentResponse> getById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            Appointment a = appointmentSvc.getById(id);
            if (!d.getCalendar().getId().equals(a.getCalendar().getId())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            Patient p = patientSvc.getByEmail(userDetails.getUsername());
            Appointment a = appointmentSvc.getById(id);
            if (!p.getId().equals(a.getMedicalFolder().getPatient().getId())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return ResponseEntity.ok(mapper.toAppointmentResponse(appointmentSvc.getById(id)));
    }
}
