package epicode.it.healthdesk.entities.calendar.opening_day;

import epicode.it.healthdesk.entities.calendar.Calendar;

import epicode.it.healthdesk.entities.calendar.opening_day.dto.OpeningDayUpdateRequest;
import epicode.it.healthdesk.entities.calendar.time_range.TimeRange;
import epicode.it.healthdesk.entities.calendar.time_range.TimeRangeRepo;
import epicode.it.healthdesk.entities.calendar.time_range.TimeSlot;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class OpeningDaySvc {
    private final OpeningDayRepo dayRepo;
    private final TimeRangeRepo timeRangeRepo;


    @Transactional
    public List<OpeningDay> generateDays(Calendar c) {
        List<OpeningDay> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            OpeningDay day = new OpeningDay();
            day.setDayName(DayOfWeek.values()[i]);
            day.setIsActive(false);
            day.setCalendar(c);
            days.add(day);
        }
        return dayRepo.saveAll(days);
    }

    public OpeningDay getById(Long id) {
        return dayRepo.findById(id).orElse(null);
    }

    public OpeningDay save(OpeningDay day) {
        return dayRepo.save(day);
    }

    public OpeningDay getByNameAndCalendarId(Long calendarId, DayOfWeek dayName) {
        return dayRepo.findFirstByNameAndCalendarId(calendarId, dayName);
    }


    public List<OpeningDay> update(Long id, @Valid List<OpeningDayUpdateRequest> request) {
        List<OpeningDay> days = new ArrayList<>();
        request.forEach(r -> {
            OpeningDay day = getByNameAndCalendarId(id, DayOfWeek.valueOf(r.getDayName()));
            BeanUtils.copyProperties(r, day);
            day=dayRepo.save(day);
            days.add(day);
        });
        return days;
    }

    @Transactional
    public OpeningDay addRange(Long id, @Valid OpeningDayUpdateRequest request) {
        OpeningDay day = getByNameAndCalendarId(id, DayOfWeek.valueOf(request.getDayName()));
        TimeRange slot = new TimeRange();
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setOpeningDay(day);
        timeRangeRepo.save(slot);
        day.getRanges().add(slot);
        return dayRepo.save(day);
    }

    public List<TimeSlot> getSlots(OpeningDay day) {
        List<TimeSlot> slots = new ArrayList<>();

        if (day.getStartTime() != null && day.getEndTime() != null) {
            LocalTime current = day.getStartTime();
            while (current.plusHours(1).isBefore(day.getEndTime()) || current.plusHours(1).equals(day.getEndTime())) {
                slots.add(new TimeSlot(current, current.plusHours(1)));
                current = current.plusHours(1);
            }
        }

        return slots;

    }

    public List<TimeSlot> getExtraRange(OpeningDay day) {
        List<TimeSlot> slots = new ArrayList<>();
        if(day.getRanges().size()>0){
            day.getRanges().forEach(r -> {
                LocalTime startTime = r.getStartTime();
                LocalTime endTime = r.getEndTime();
                while (startTime.plusHours(1).isBefore(r.getEndTime()) || startTime.plusHours(1).equals(r.getEndTime())) {
                    slots.add(new TimeSlot(startTime, startTime.plusHours(1)));
                    startTime = startTime.plusHours(1);
                }
            });
        }
        return slots;
    }
}
