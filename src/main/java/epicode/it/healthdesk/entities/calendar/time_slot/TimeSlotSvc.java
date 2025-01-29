package epicode.it.healthdesk.entities.calendar.time_slot;

import epicode.it.healthdesk.entities.calendar.active_day.ActiveDay;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TimeSlotSvc {
    private final TimeSlotRepo timeSlotRepo;

    public TimeSlot findByStartTimeAndActiveDayId(LocalTime startTime, Long dayId) {
        return timeSlotRepo.findByStartTimeAndActiveDayId(startTime, dayId).orElse(null);
    }

    public TimeSlot create(ActiveDay day, LocalTime startTime, LocalTime endTime) {
        if(findByStartTimeAndActiveDayId(startTime, day.getId()) != null) {
            throw new EntityExistsException("Slot già esistente per il giorno selezionato");
        }
        TimeSlot slot = new TimeSlot();
        slot.setStartTime(LocalTime.of(startTime.getHour(), startTime.getMinute()));
        slot.setEndTime(LocalTime.of(endTime.getHour(), endTime.getMinute()));
        slot.setActiveDay(day);
        return timeSlotRepo.save(slot);
    }
}
