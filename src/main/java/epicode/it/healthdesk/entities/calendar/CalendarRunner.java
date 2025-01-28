package epicode.it.healthdesk.entities.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(10)
@RequiredArgsConstructor
public class CalendarRunner {
    private final CalendarSvc calendarSvc;
}
