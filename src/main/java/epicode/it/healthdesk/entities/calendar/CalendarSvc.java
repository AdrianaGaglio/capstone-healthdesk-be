package epicode.it.healthdesk.entities.calendar;

import epicode.it.healthdesk.entities.calendar.active_day.ActiveDay;
import epicode.it.healthdesk.entities.calendar.calendar_setting.CalendarSettings;
import epicode.it.healthdesk.entities.calendar.calendar_setting.CalendarSettingsSvc;
import epicode.it.healthdesk.entities.calendar.time_slot.dto.TimeSlotRequest;
import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;
import java.util.TreeSet;

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
    public Calendar addActiveDay(Long calendarId, String dayName) {
        Calendar c = getById(calendarId);
        c.getSettings().getActiveDays().add(settingsSvc.addActiveDay(c.getSettings(), dayName));
        return c;
    }

    public Calendar addSlot(Long calendarId, TimeSlotRequest newSlot) {
        Calendar c = getById(calendarId);
        CalendarSettings settings = c.getSettings();
        ActiveDay day = settings.getActiveDays().stream().filter(d -> d.getId() == newSlot.getDayId()).findFirst().orElse(null);
        day.getSlots().add(settingsSvc.addSlotToActiveDay(day, newSlot));
        return c;
    }
}
