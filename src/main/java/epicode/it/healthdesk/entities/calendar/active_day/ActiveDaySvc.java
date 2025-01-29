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

    public ActiveDay getById(Long id) {
        return activeDayRepo.findById(id).orElse(null);
    }

    public ActiveDay save(ActiveDay day) {
        return activeDayRepo.save(day);
    }
}
