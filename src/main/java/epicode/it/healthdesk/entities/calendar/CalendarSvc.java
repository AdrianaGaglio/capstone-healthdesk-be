package epicode.it.healthdesk.entities.calendar;

import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDaySvc;
import epicode.it.healthdesk.entities.calendar.opening_day.dto.OpeningDayUpdateRequest;
import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarSvc {
    private final CalendarRepo calendarRepo;
    private final OpeningDaySvc daySvc;

    public Calendar getById(Long id) {
        return calendarRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Calendario non trovato"));
    }

    public Calendar getByDoctor(Doctor d) {
        return calendarRepo.findByDoctor(d).orElseThrow(() -> new EntityNotFoundException("Calendario non trovato"));
    }

    @Transactional
    public Calendar create(Doctor d) {
        Calendar c = new Calendar();
        c.setDoctor(d);
        c.getDays().addAll(daySvc.generateDays(c));
        return calendarRepo.save(c);
    }

    @Transactional
    public Calendar updateDay(Long id, OpeningDayUpdateRequest request) {
        Calendar c = getById(id);
        daySvc.update(c.getId(), request);
        return c;
    }

    @Transactional
    public Calendar addRange(Long id, OpeningDayUpdateRequest request) {
        Calendar c = getById(id);
        daySvc.addRange(c.getId(), request);
        return c;
    }
}
