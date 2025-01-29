package epicode.it.healthdesk.entities.calendar;

import epicode.it.healthdesk.entities.calendar.active_day.ActiveDay;
import epicode.it.healthdesk.entities.calendar.active_day.DayName;
import epicode.it.healthdesk.entities.calendar.calendar_setting.CalendarSettings;
import epicode.it.healthdesk.entities.calendar.calendar_setting.CalendarSettingsSvc;
import epicode.it.healthdesk.entities.calendar.time_slot.dto.TimeSlotRequest;
import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarSvc {
    private final CalendarRepo calendarRepo;
    private final CalendarSettingsSvc settingsSvc;

    public Calendar getById(Long id) {
        return calendarRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Calendario non trovato"));
    }

    @Transactional
    public Calendar create(Doctor d) {
        Calendar c = new Calendar();
        c.setDoctor(d);
        c.setSettings(settingsSvc.create(c));
        return calendarRepo.save(c);
    }

    @Transactional
    public Calendar manageDay(Long calendarId, String dayName) {
        Calendar c = getById(calendarId);
        ActiveDay day = c.getSettings().getActiveDays().stream().filter(d -> d.getDayName() == DayName.valueOf(dayName.toUpperCase())).findFirst().orElse(null);
        settingsSvc.manageDay(day);
        return getById(calendarId);
    }

    public Calendar addSlot(Long calendarId, TimeSlotRequest newSlot) {
        Calendar c = getById(calendarId);
        CalendarSettings settings = c.getSettings();
        ActiveDay day = settings.getActiveDays().stream().filter(d -> d.getId() == newSlot.getDayId()).findFirst().orElse(null);
        day.getSlots().add(settingsSvc.addSlotToActiveDay(day, newSlot));
        return c;
    }
}
