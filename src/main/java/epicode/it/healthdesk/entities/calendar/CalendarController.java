package epicode.it.healthdesk.entities.calendar;


import com.google.api.Http;
import epicode.it.healthdesk.entities.calendar.dto.CalendarMapper;
import epicode.it.healthdesk.entities.calendar.dto.CalendarResponse;
import epicode.it.healthdesk.entities.calendar.dto.CalendarResponseForPatient;
import epicode.it.healthdesk.entities.calendar.dto.HolidayRequest;
import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDaySvc;
import epicode.it.healthdesk.entities.calendar.opening_day.dto.OpeningDayUpdateRequest;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import lombok.RequiredArgsConstructor;

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
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarSvc calendarSvc;
    private final CalendarMapper mapper;
    private final OpeningDaySvc daySvc;
    private final DoctorSvc doctorSvc;

    // calendario per il medico
    @GetMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<CalendarResponse> get(@AuthenticationPrincipal UserDetails userDetails) {

        // resistuisce l'agenda del medico
        Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.getByDoctor(d)), HttpStatus.OK);
    }

    // accessibile a pazienti (anche non loggati) e agli admin
    @GetMapping("/{id}")
    public ResponseEntity<CalendarResponse> getById(@PathVariable Long id) {
        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.getById(id)), HttpStatus.OK);
    }

    // calendario per il paziente (anche non loggato) ❌ rimuovere (modificare la chiamata su fe con quella sopra)
    @GetMapping("/for-patient")
    public ResponseEntity<CalendarResponseForPatient> getForPatient() {
        return new ResponseEntity<>(mapper.toCalendarResponseForPatient(calendarSvc.getForPatient()), HttpStatus.OK);
    }

    // gestione dei giorni attivi
    @PostMapping("/{id}/manage-days")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<CalendarResponse> manageDaySettings(@PathVariable Long id, @RequestBody List<OpeningDayUpdateRequest> request, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il medico è proprietario dell'agenda
        Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
        if (!d.getCalendar().getId().equals(id)) {
            throw new AccessDeniedException("Accesso negato");
        }

        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.updateDay(id, request)), HttpStatus.ACCEPTED);
    }

    // gestione degli orari attivi
    @PostMapping("/{id}/add-time-range")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<CalendarResponse> addTimeRange(@PathVariable Long id, @RequestBody OpeningDayUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il medico è proprietario dell'agenda
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            if (!d.getCalendar().getId().equals(id)) {
                throw new AccessDeniedException("Accesso negato");
            }
        }
        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.addRange(id, request)), HttpStatus.ACCEPTED);
    }

    // modifica lo stato del calendario (attivo/disattivo)
    @PutMapping("/{id}/change-status")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<CalendarResponse> changeStatus(@PathVariable Long id, @RequestParam boolean isActive, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il medico è proprietario dell'agenda
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            if (!d.getCalendar().getId().equals(id)) {
                throw new AccessDeniedException("Accesso negato");
            }
        }
        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.changeStatus(id, isActive)), HttpStatus.ACCEPTED);
    }

    // gestione periodi di sospensione dell'agenda
    @PutMapping("{id}/holiday")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<CalendarResponse> handleOnHoliday(@PathVariable Long id, @RequestBody HolidayRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        // controlla se il medico è proprietario dell'agenda
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            Doctor d = doctorSvc.getByEmail(userDetails.getUsername());
            if (!d.getCalendar().getId().equals(id)) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.handleOnHoliday(id, request)), HttpStatus.ACCEPTED);
    }
}
