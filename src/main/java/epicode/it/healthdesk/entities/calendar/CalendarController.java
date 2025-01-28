package epicode.it.healthdesk.entities.calendar;

import epicode.it.healthdesk.entities.calendar.active_day.ActiveDaySvc;
import epicode.it.healthdesk.entities.calendar.dto.CalendarMapper;
import epicode.it.healthdesk.entities.calendar.dto.CalendarResponse;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<CalendarResponse> addDay(@PathVariable Long id, @RequestParam String dayName) {
        Calendar c = calendarSvc.getById(id);
        return new ResponseEntity<>(mapper.toCalendarResponse(activeDaySvc.create(c, dayName)), HttpStatus.CREATED);
    }

}
