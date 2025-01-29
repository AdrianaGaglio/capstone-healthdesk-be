package epicode.it.healthdesk.entities.calendar.calendar_setting;

import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.calendar.active_day.ActiveDay;
import epicode.it.healthdesk.entities.calendar.active_day.ActiveDaySvc;
import epicode.it.healthdesk.entities.calendar.active_day.DayName;
import epicode.it.healthdesk.entities.calendar.time_slot.TimeSlot;
import epicode.it.healthdesk.entities.calendar.time_slot.TimeSlotSvc;
import epicode.it.healthdesk.entities.calendar.time_slot.dto.TimeSlotRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarSettingsSvc {
    private final CalendarSettingsRepo settingsRepo;
    private final ActiveDaySvc daySvc;
    private final TimeSlotSvc slotSvc;

    @Transactional
    public CalendarSettings create(Calendar c) {
        CalendarSettings settings = new CalendarSettings(c);
        settings.getActiveDays().addAll(daySvc.generateDays(settings));
        return settingsRepo.save(settings);
    }

    @Transactional
    public ActiveDay manageDay(ActiveDay day) {
        if (day.getIsActive()) {
            day.setIsActive(false);
        } else {
            day.setIsActive(true);
        }
        return daySvc.save(day);
    }

    @Transactional
    public TimeSlot addSlotToActiveDay(ActiveDay day, TimeSlotRequest request) {
        return slotSvc.create(day, request.getStartTime(), request.getEndTime());

    }
}
