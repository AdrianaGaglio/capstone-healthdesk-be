package epicode.it.healthdesk.entities.calendar;

import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.appointment.AppointmentRepo;
import epicode.it.healthdesk.utilities.email.EmailMapper;
import epicode.it.healthdesk.utilities.email.EmailRequest;
import epicode.it.healthdesk.utilities.email.EmailSvc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final EmailMapper mapper;
    private final EmailSvc emailSvc;

    @Value("${spring.mail.username}")
    private String from;

    // ogni giorno alle 8, controllo se ci sono appuntamenti PENDING per l'indomani
    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public void requestConfirmation() {
        LocalDateTime startOfTomorrow = LocalDate.now().plusDays(1).atStartOfDay(); // domani all'inizio del giorno
        LocalDateTime endOfTomorrow = startOfTomorrow.plusDays(1).minusSeconds(1); // domani alla fine del giorno
        List<Appointment> appointments = repo.findAllBetween(startOfTomorrow, endOfTomorrow); // lista di appuntamenti pending di domani
        appointments.forEach(a -> {
            // per ogni appuntamento trovato, mando la mail di richiesta conferma
            EmailRequest request = new EmailRequest();
            request.setTo(a.getMedicalFolder().getPatient().getAppUser().getEmail());
            request.setFrom(from);
            request.setSubject("Health Desk - Richiesta conferma appuntamento");
            request.setBody(mapper.toAppConfirmation(a));
            emailSvc.sendEmailHtml(request);
        });

    }

    // ogni giorno alle 7 controllo se ci sono appuntamenti pending per oggi
    @Scheduled(cron = "0 0 7 * * ?")
    @Transactional
    public void requestConfirmationToday() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay(); // inizio della giornata odierna
        LocalDateTime endOfToday = startOfToday.plusDays(1).minusSeconds(1); // fine della giornata odierna
        List<Appointment> appointments = repo.findAllBetween(startOfToday, endOfToday); // lista di appuntamenti pending della giornata odierna
        appointments.forEach(a -> {
            // per ogni appuntamento trovato, mando email di richiesta conferma
            EmailRequest request = new EmailRequest();
            request.setTo(a.getMedicalFolder().getPatient().getAppUser().getEmail());
            request.setFrom(from);
            request.setSubject("Health Desk - Richiesta conferma appuntamento");
            request.setBody(mapper.toAppConfirmation(a));
            emailSvc.sendEmailHtml(request);
        });
    }


    // ogni giorno alle 7 controllo se ci sono appuntamenti pending per la giornata di ieri
    @Scheduled(cron = "0 55 11  * * ?")
    @Transactional
    public void requestConfirmationDayAfter() {
        LocalDateTime startOfYesterday = LocalDate.now().minusDays(1).atStartOfDay(); // inizio della giornata di ieri

        LocalDateTime endOfYesterday = startOfYesterday.plusDays(1).minusSeconds(1); // fine della giornata di ieri

        List<Appointment> appointments = repo.findAllBetween(startOfYesterday, endOfYesterday); // lista di appuntamenti pending della giornata di ieri
        appointments.forEach(a -> {
            System.out.println(a.getStartDate() + " " + a.getStatus());
            // per ogni appuntamento trovato, mando email di richiesta conferma
            EmailRequest request = new EmailRequest();
            request.setTo(from);
            request.setFrom(from);
            request.setSubject("Health Desk - Richiesta conferma appuntamento");
            request.setBody(mapper.toAppConfirmationForDoctor(a));
            emailSvc.sendEmailHtml(request);
        });
    }



}
