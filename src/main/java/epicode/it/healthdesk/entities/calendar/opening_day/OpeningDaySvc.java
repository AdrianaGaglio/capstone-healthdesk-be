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

    public List<OpeningDay> getByCalendar(Calendar calendar) {
        return dayRepo.findAllByCalendar(calendar);
    }

    // genero i giorni della settimana (chiamato contestualmente alla creazione del calendario)
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

    // per cercare il giorno in base al nome
    public OpeningDay getByNameAndCalendarId(Long calendarId, DayOfWeek dayName) {
        return dayRepo.findFirstByNameAndCalendarId(calendarId, dayName);
    }

    // modifica dettagli del giorno della settimana
    @Transactional
    public List<OpeningDay> update(Long id, @Valid List<OpeningDayUpdateRequest> request) {
        List<OpeningDay> days = new ArrayList<>();
        // ciclo la lista dei giorni della settimana
        request.forEach(r -> {
            // ottengo il giorno a partire dal suo nome
            OpeningDay day = getByNameAndCalendarId(id, DayOfWeek.valueOf(r.getDayName()));

            // per ogni giorno imposto i vari parametri (stato, ora lavorativa di inizio e fine)
            BeanUtils.copyProperties(r, day);
            if (r.getExtraRange() != null) {
                if (!timeRangeRepo.existsByOpeningDay(day)) {
                    TimeRange range = new TimeRange();
                    BeanUtils.copyProperties(r.getExtraRange(), range);
                    range.setOpeningDay(day);
                    timeRangeRepo.save(range);
                } else {
                    // se sono definiti range extra nella richiesta, credo il nuovo range impostando le sue proprietà
                    TimeRange range = timeRangeRepo.findByOpeningDay(day);
                    BeanUtils.copyProperties(r.getExtraRange(), range);
                    timeRangeRepo.save(range);
                }
            } else {
                // se non ci sono time range nella richiesta di modifica, cancello tutti quelli salvati a db
                if (timeRangeRepo.existsByOpeningDay(day)) {
                    timeRangeRepo.delete(timeRangeRepo.findByOpeningDay(day));
                }
            }
            day = dayRepo.save(day);
            days.add(day);
        });
        return days;
    }

    // per aggiungere un extra range orario alla giornata
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

    // genera gli slot orari disponibili per la giornata (serve per la risposta al front-end)
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

    // genera gli gli slot definiti nell'extra range (serve per la risposta al front end)
    public List<TimeSlot> getExtraRange(OpeningDay day) {
        List<TimeSlot> slots = new ArrayList<>();
        if (day.getRanges().size() > 0) {
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
