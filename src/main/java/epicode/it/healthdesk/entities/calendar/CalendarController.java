package epicode.it.healthdesk.entities.calendar;

import epicode.it.healthdesk.entities.calendar.active_day.ActiveDaySvc;
import epicode.it.healthdesk.entities.calendar.dto.CalendarMapper;
import epicode.it.healthdesk.entities.calendar.dto.CalendarResponse;
import epicode.it.healthdesk.entities.calendar.time_slot.dto.TimeSlotRequest;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/{id}/new-day")
    public ResponseEntity<CalendarResponse> addActiveDay(@PathVariable Long id, @RequestParam String dayName) {
        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.addActiveDay(id, dayName)), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/new-slot")
    public ResponseEntity<CalendarResponse> addTimeSlot(@PathVariable Long id, @RequestBody TimeSlotRequest request) {
        return new ResponseEntity<>(mapper.toCalendarResponse(calendarSvc.addSlot(id, request)), HttpStatus.CREATED);
    }
}
