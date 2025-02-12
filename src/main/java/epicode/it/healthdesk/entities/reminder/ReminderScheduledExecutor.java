package epicode.it.healthdesk.entities.reminder;

import epicode.it.healthdesk.utilities.email.EmailMapper;
import epicode.it.healthdesk.utilities.email.EmailRequest;
import epicode.it.healthdesk.utilities.email.EmailSvc;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReminderScheduledExecutor {
    private final ReminderSvc reminderSvc;
    private final EmailMapper emailMapper;
    private final EmailSvc emailSvc;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    @Transactional
    @PostConstruct
    public void startRepeatingTask() {

        EmailRequest mail = new EmailRequest();
        mail.setSubject("Health Desk - Promemoria");

        executorService.scheduleAtFixedRate(() -> {
            List<Reminder> reminders = reminderSvc.getAll();

            LocalDate today = LocalDate.now();

            // inizio e fine settimana corrente
            LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            // inizio e fine mese corrente
            LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
            LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

            try {

                if(reminders.size() > 0) {
                    reminders.forEach(r -> {

                        if(r.getFrequency().equals(Frequency.DAILY)) {
                            // controllo se è il giorno prima dell'appuntamento e non è già stato inviato il promemoria
                            boolean alreadySent = r.getLastSent() != null && r.getLastSent().equals(today);
                            if(today.plusDays(1).equals(r.getStartDate()) && (!alreadySent || r.getLastSent() == null)) {
                                mail.setTo(r.getMedicalFolder().getPatient().getAppUser().getEmail());
                                mail.setBody(emailMapper.toReminder(r.getMedicalFolder().getPatient()));
                                emailSvc.sendEmailHtml(mail);
                                r.setLastSent(today);
                                reminderSvc.save(r);
                            }
                        }
                        
                        if(r.getFrequency().equals(Frequency.WEEKLY)) {
                            // controllo se sono 3 giorni prima dell'appuntamento e non è stato inviato il promemoria questa settimana
                            boolean alreadySent = r.getLastSent() != null && r.getLastSent().isAfter(startOfWeek) && r.getLastSent().isBefore(endOfWeek) ;
                            if(today.plusDays(3).equals(r.getStartDate()) && (!alreadySent || r.getLastSent() == null)) {
                                mail.setTo(r.getMedicalFolder().getPatient().getAppUser().getEmail());
                                mail.setBody(emailMapper.toReminder(r.getMedicalFolder().getPatient()));
                                emailSvc.sendEmailHtml(mail);
                                r.setLastSent(today);
                                reminderSvc.save(r);
                            }
                        }
                        
                        if(r.getFrequency().equals(Frequency.MONTHLY)) {
                            // controllo se sono 7 giorni prima dell'appuntamento e non è stato inviato il promemoria
                            boolean alreadySent = r.getLastSent()!= null && r.getLastSent().isAfter(startOfMonth) && r.getLastSent().isBefore(endOfMonth);
                            if(today.plusDays(7).equals(r.getStartDate()) && (!alreadySent || r.getLastSent() == null)) {
                                mail.setTo(r.getMedicalFolder().getPatient().getAppUser().getEmail());
                                mail.setBody(emailMapper.toReminder(r.getMedicalFolder().getPatient()));
                                emailSvc.sendEmailHtml(mail);
                                r.setLastSent(today);
                                reminderSvc.save(r);
                            }
                        }
                        
                        if(r.getFrequency().equals(Frequency.QUARTERLY)) {

                            if(today.plusDays(15).equals(r.getStartDate()) && r.getLastSent() == null) {
                                mail.setTo(r.getMedicalFolder().getPatient().getAppUser().getEmail());
                                mail.setBody(emailMapper.toReminder(r.getMedicalFolder().getPatient()));
                                emailSvc.sendEmailHtml(mail);
                                r.setLastSent(today);
                                reminderSvc.save(r);
                            }
                        }
                        
                        if(r.getFrequency().equals(Frequency.SEMIANNUAL)) {
                            if(today.plusDays(30).equals(r.getStartDate()) && r.getLastSent() == null) {
                                mail.setTo(r.getMedicalFolder().getPatient().getAppUser().getEmail());
                                mail.setBody(emailMapper.toReminder(r.getMedicalFolder().getPatient()));
                                emailSvc.sendEmailHtml(mail);
                                r.setLastSent(today);
                                reminderSvc.save(r);
                            }
                            if(today.plusDays(15).equals(r.getStartDate()) && r.getLastSent().plusDays(15).equals(today)) {
                                mail.setTo(r.getMedicalFolder().getPatient().getAppUser().getEmail());
                                mail.setBody(emailMapper.toReminder(r.getMedicalFolder().getPatient()));
                                emailSvc.sendEmailHtml(mail);
                                r.setLastSent(today);
                                reminderSvc.save(r);
                            }
                        }
                        
                    });
                }


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }, 0, 30, TimeUnit.MINUTES); // Esegue ogni giorno
    }

    public void shutdown() {
        System.out.println("Arresto del ScheduledExecutorService...");
        executorService.shutdown();
    }
}
