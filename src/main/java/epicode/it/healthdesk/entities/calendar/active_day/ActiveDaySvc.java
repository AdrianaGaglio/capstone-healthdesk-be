package epicode.it.healthdesk.entities.calendar.active_day;

import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.calendar.CalendarSvc;
import epicode.it.healthdesk.entities.calendar.calendar_setting.CalendarSettings;
import epicode.it.healthdesk.entities.calendar.time_slot.TimeSlot;
import epicode.it.healthdesk.entities.calendar.time_slot.TimeSlotSvc;
import epicode.it.healthdesk.entities.calendar.time_slot.dto.TimeSlotRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActiveDaySvc {
    private final ActiveDayRepo activeDayRepo;

    @Transactional
    public List<ActiveDay> generateDays(CalendarSettings settings) {
        List<ActiveDay> days = new ArrayList<>();
        for(int i = 0; i < 7; i++) {
            ActiveDay day = new ActiveDay();
            day.setDayName(DayName.values()[i]);
            day.setIsActive(false);
            day.setSettings(settings);
            days.add(day);
        }
        return activeDayRepo.saveAll(days);
    }

    public ActiveDay getById(Long id) {
        return activeDayRepo.findById(id).orElse(null);
    }

    public ActiveDay save(ActiveDay day) {
        return activeDayRepo.save(day);
    }
}
