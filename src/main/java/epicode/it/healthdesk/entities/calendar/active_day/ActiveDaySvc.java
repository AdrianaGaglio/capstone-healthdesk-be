package epicode.it.healthdesk.entities.calendar.active_day;

import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.calendar.CalendarSvc;
import epicode.it.healthdesk.entities.calendar.time_slot.TimeSlot;
import epicode.it.healthdesk.entities.calendar.time_slot.TimeSlotSvc;
import epicode.it.healthdesk.entities.calendar.time_slot.dto.TimeSlotRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ActiveDaySvc {
    private final ActiveDayRepo activeDayRepo;
    private final CalendarSvc calendarSvc;
    private final TimeSlotSvc timeSlotSvc;

    public ActiveDay getById(Long id) {
        return activeDayRepo.findById(id).orElse(null);
    }

    @Transactional
    public Calendar create(Calendar c, String dayName) {
        ActiveDay day = new ActiveDay();
        day.setDayName(DayName.valueOf(dayName.toUpperCase()));
        day.setCalendar(c);
        activeDayRepo.save(day);
        return c;
    }

    @Transactional
    public Calendar addSlot(Long id, TimeSlotRequest request) {
        ActiveDay day = getById(id);
        Calendar c = calendarSvc.getById(day.getCalendar().getId());
        day.getSlots().add(timeSlotSvc.create(day, request.getStartTime(), request.getEndTime()));
        return c;
    }
}
