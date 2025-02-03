package epicode.it.healthdesk.entities.calendar;


import epicode.it.healthdesk.entities.calendar.dto.CalendarMapper;
import epicode.it.healthdesk.entities.calendar.dto.CalendarResponse;
import epicode.it.healthdesk.entities.calendar.dto.CalendarResponseForPatient;
import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDaySvc;
import epicode.it.healthdesk.entities.calendar.opening_day.dto.OpeningDayUpdateRequest;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarSvc calendarSvc;
    private final CalendarMapper mapper;
    private final OpeningDaySvc daySvc;
    private final DoctorSvc doctorSvc;

    @GetMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<CalendarResponse> get(@AuthenticationPrincipal UserDetails userDetails) {

        Doctor d = doctorSvc.getByEmail(userDetails.getUsername());

        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.getByDoctor(d)), HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<CalendarResponse> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(mapper.toCalendarResponse(calendarSvc.getById(id)));
//    }

    @GetMapping("/for-patient")
    public ResponseEntity<CalendarResponseForPatient> getForPatient() {
        return ResponseEntity.ok(mapper.toCalendarResponseForPatient(calendarSvc.getForPatient()));
    }

    @PostMapping("/{id}/manage-days")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<CalendarResponse> manageDaySettings(@PathVariable Long id, @RequestBody List<OpeningDayUpdateRequest> request) {

        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.updateDay(id, request)), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/add-time-range")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<CalendarResponse> addTimeRange(@PathVariable Long id, @RequestBody OpeningDayUpdateRequest request) {
        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.addRange(id, request)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/change-status")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<CalendarResponse> changeStatus(@PathVariable Long id, @RequestParam boolean isActive) {
        return ResponseEntity.ok(mapper.toCalendarResponse(calendarSvc.changeStatus(id, isActive)));
    }
}
