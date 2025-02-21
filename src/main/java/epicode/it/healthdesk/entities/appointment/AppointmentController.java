package epicode.it.healthdesk.entities.appointment;

import epicode.it.healthdesk.entities.appointment.dto.*;
import epicode.it.healthdesk.entities.calendar.dto.CalendarMapper;
import epicode.it.healthdesk.entities.calendar.dto.CalendarResponse;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.patient.PatientSvc;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<AppointmentResponse> create(@RequestBody AppointmentRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(mapper.toAppointmentResponse(appointmentSvc.create(request, userDetails)), HttpStatus.CREATED);
    }

    @PostMapping("/block-slot") // per il medico
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AppointmentResponse> blockSlot(@RequestBody BlockedSlotRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        return new ResponseEntity<>(mapper.toAppointmentResponse(appointmentSvc.blockedSlot(userDetails, request)), HttpStatus.ACCEPTED);
    }

    @PostMapping("/unlock-slot")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, String>> unlock(@RequestParam Long id, @AuthenticationPrincipal UserDetails userDetails) {

        appointmentSvc.unlockSlot(userDetails, id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Slot orario sbloccato");

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    // prossimi appuntamenti medico
    @GetMapping("/next/{calendarId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Page<AppointmentResponse>> findNextByCalendar(@PathVariable Long calendarId, @ParameterObject Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla che il medico è proprietario di quell'agenda
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
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<CalendarResponse> update(@PathVariable Long id, @RequestBody Appointment request, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il medico è proprietario di quell'appuntamento
        Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
        Appointment a = appointmentSvc.getById(id);
        if (!d.getCalendar().getId().equals(a.getCalendar().getId())) {
            throw new AccessDeniedException("Accesso negato");
        }

        return new ResponseEntity<>(calendarMapper.toCalendarResponse((appointmentSvc.update(id, request))), HttpStatus.OK);
    }

    // annulla appuntamento
    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ResponseEntity<AppointmentResponseForMedicalFolder> cancel(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il paziente o il medico sono proprietari di quell'appuntamento
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            Patient p = patientSvc.getByEmail(userDetails.getUsername());
            Appointment a = appointmentSvc.getById(id);
            if (!p.getId().equals(a.getMedicalFolder().getPatient().getId())) {
                throw new AccessDeniedException("Accesso negato");
            }
        } else if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            Appointment a = appointmentSvc.getById(id);
            if (!d.getCalendar().getId().equals(a.getCalendar().getId())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return new ResponseEntity<>(mapper.toAppResponseForMF(appointmentSvc.cancelApp(id, userDetails)), HttpStatus.OK);
    }

    // conferma appuntamento
    @PutMapping("/confirm/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ResponseEntity<AppointmentResponseForMedicalFolder> confirm(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il paziente o il medico sono proprietari di quell'appuntamento
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            Patient p = patientSvc.getByEmail(userDetails.getUsername());
            Appointment a = appointmentSvc.getById(id);
            if (!p.getId().equals(a.getMedicalFolder().getPatient().getId())) {
                throw new AccessDeniedException("Accesso negato");
            }
        } else if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            Appointment a = appointmentSvc.getById(id);
            if (!d.getCalendar().getId().equals(a.getCalendar().getId())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return new ResponseEntity<>(mapper.toAppResponseForMF(appointmentSvc.confirmApp(id, userDetails)), HttpStatus.OK);
    }

    // modifica data appuntamento per il paziente
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponseForMedicalFolder> updateDate(@PathVariable Long id, @RequestBody AppointmentDateUpdate request, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il paziente è proprietario di quell'appuntamento
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

        // controlla se il medico è proprietario di quell'appuntamento
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            Appointment a = appointmentSvc.getById(id);
            if (!d.getCalendar().getId().equals(a.getCalendar().getId())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        // controlla se il paziente è proprietario di quell'appuntamento
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            Patient p = patientSvc.getByEmail(userDetails.getUsername());
            Appointment a = appointmentSvc.getById(id);
            if (!p.getId().equals(a.getMedicalFolder().getPatient().getId())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return ResponseEntity.ok(mapper.toAppointmentResponse(appointmentSvc.getById(id)));
    }

    // serve per controllare lato front l'id del paziente di un appuntamento
    @GetMapping("/{id}/check")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Long>> getById(@PathVariable Long id) {
        Appointment app = appointmentSvc.getById(id);
        Map<String, Long> response = new HashMap<>();
        response.put("patientId", app.getMedicalFolder().getPatient().getId());
        return ResponseEntity.ok(response);
    }
}
