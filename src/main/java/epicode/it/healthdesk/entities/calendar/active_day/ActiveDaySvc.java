package epicode.it.healthdesk.entities.calendar.active_day;

import epicode.it.healthdesk.entities.calendar.Calendar;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActiveDaySvc {
    private final ActiveDayRepo activeDayRepo;

    @Transactional
    public Calendar create(Calendar c, String dayName) {
        ActiveDay day = new ActiveDay();
        day.setDayName(DayName.valueOf(dayName.toUpperCase()));
        day.setCalendar(c);
        activeDayRepo.save(day);
        return c;
    }
}
