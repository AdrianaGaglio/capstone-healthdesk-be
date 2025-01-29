package epicode.it.healthdesk.entities.calendar.calendar_setting;

import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.calendar.CalendarSvc;
import epicode.it.healthdesk.entities.calendar.active_day.ActiveDay;
import epicode.it.healthdesk.entities.calendar.active_day.ActiveDayRepo;
import epicode.it.healthdesk.entities.calendar.active_day.ActiveDaySvc;
import epicode.it.healthdesk.entities.calendar.active_day.DayName;
import epicode.it.healthdesk.entities.calendar.time_slot.TimeSlot;
import epicode.it.healthdesk.entities.calendar.time_slot.TimeSlotRepo;
import epicode.it.healthdesk.entities.calendar.time_slot.TimeSlotSvc;
import epicode.it.healthdesk.entities.calendar.time_slot.dto.TimeSlotRequest;
import jakarta.persistence.EntityExistsException;
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
        return settingsRepo.save(new CalendarSettings(c));
    }

    @Transactional
    public ActiveDay addActiveDay(CalendarSettings settings, String dayName) {
        if(settings.getActiveDays().stream().anyMatch(d -> d.getDayName().equals(DayName.valueOf(dayName.toUpperCase())))) {
            throw new EntityExistsException("Giornata già inserita per questo calendario");
        };
        ActiveDay day = new ActiveDay();
        day.setDayName(DayName.valueOf(dayName.toUpperCase()));
        day.setSettings(settings);
        return daySvc.save(day);
    }

    @Transactional
    public TimeSlot addSlotToActiveDay(ActiveDay day, TimeSlotRequest request) {
        return slotSvc.create(day, request.getStartTime(), request.getEndTime());

    }
}
