package epicode.it.healthdesk.entities.calendar;

import epicode.it.healthdesk.entities.calendar.active_day.ActiveDaySvc;
import epicode.it.healthdesk.entities.calendar.dto.CalendarMapper;
import epicode.it.healthdesk.entities.calendar.dto.CalendarResponse;
import epicode.it.healthdesk.entities.calendar.time_slot.dto.TimeSlotRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarMapper mapper;
    private final CalendarSvc calendarSvc;
    private final ActiveDaySvc activeDaySvc;

    @GetMapping("/{id}")
    public ResponseEntity<CalendarResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(mapper.toCalendarResponse(calendarSvc.getById(id)));
    }

    @PostMapping("/{id}/manage-day")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<CalendarResponse> manageDay(@PathVariable Long id, @RequestBody String dayName, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("DOCTOR"))) {
            if (!calendarSvc.getById(id).getDoctor().getAppUser().getEmail().equals(userDetails.getUsername())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.manageDay(id, dayName)), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/new-slot")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<CalendarResponse> addTimeSlot(@PathVariable Long id, @RequestBody TimeSlotRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("DOCTOR"))) {
            if (!calendarSvc.getById(id).getDoctor().getAppUser().getEmail().equals(userDetails.getUsername())) {
                throw new AccessDeniedException("Accesso negato");
            }
        }

        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.addSlot(id, request)), HttpStatus.CREATED);
    }
}
