package epicode.it.healthdesk.entities.calendar;

import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.appointment.AppointmentRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentMonitor {
    private final AppointmentRepo repo;

    @Scheduled(cron = "0 00 7 * * ?")
    @Transactional
    public void requestConfirmation() {
        LocalDateTime today = LocalDateTime.now();
        List<Appointment> appointments = repo.findAllAfter(today);
        appointments.forEach(a -> System.out.println(a.getStartDate() + " " + a.getStatus()));
        // aggiungere invio email automatica

    }


}
