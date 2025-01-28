package epicode.it.healthdesk.entities.calendar;

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

    public Calendar getById(Long id) {
        return calendarRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Calendario non trovato"));
    }

    @Transactional
    public Calendar create(Doctor d) {
        Calendar c = new Calendar();
        c.setDoctor(d);
        return calendarRepo.save(c);
    }
}
