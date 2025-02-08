package epicode.it.healthdesk.entities.calendar;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarMonitor {
    private final CalendarRepo repo;

    @Scheduled(cron = "0 53 15 * * ?")
    @Transactional
    public void checkAndReactive() {
        LocalDate today = LocalDate.now();
        List<Calendar> calendars = repo.findActiveHolidays();
        calendars.forEach(c -> {
            if (c.getHolidayDateEnd().isBefore(today)) {
                c.setOnHoliday(false);
                c.setHolidayDateStart(null);
                c.setHolidayDateEnd(null);
                repo.save(c);
            }
        });
    }
}
