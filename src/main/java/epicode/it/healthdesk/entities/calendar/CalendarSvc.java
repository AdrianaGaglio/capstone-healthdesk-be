package epicode.it.healthdesk.entities.calendar;

import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.appointment.AppointmentRepo;
import epicode.it.healthdesk.entities.appointment.AppointmentStatus;
import epicode.it.healthdesk.entities.appointment.AppointmentSvc;
import epicode.it.healthdesk.entities.calendar.dto.HolidayRequest;
import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDaySvc;
import epicode.it.healthdesk.entities.calendar.opening_day.dto.OpeningDayUpdateRequest;
import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarSvc {
    private final CalendarRepo calendarRepo;
    private final OpeningDaySvc daySvc;

    // il sistema è predisposto per gestire più calendari
    // in questa situazione specifica il progetto prevede l'utilizzo di un solo calendario
    // metodo per ottenere il calendario per il paziente (cerca tutti i calendari a db e prende il primo)
    public Calendar getForPatient() {
        return calendarRepo.findAll().stream().findFirst().orElse(null);
    }

    // ricerca calendario per id
    public Calendar getById(Long id) {
        return calendarRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Calendario non trovato"));
    }

    // ricerca calendario per il medico, lo cerca in base al medico
    public Calendar getByDoctor(Doctor d) {
        return calendarRepo.findByDoctor(d).orElseThrow(() -> new EntityNotFoundException("Calendario non trovato"));
    }

    // crea un nuovo calendario (viene chiamata contestualmente alla registrazione di un nuovo medico)
    @Transactional
    public Calendar create(Doctor d) {
        Calendar c = new Calendar();
        c.setDoctor(d);
        c.getDays().addAll(daySvc.generateDays(c)); // genera i giorni e li assegna al calendario
        c.setIsActive(true); // di default viene creato attivo
        return calendarRepo.save(c);
    }

    // per ritornare il calendario aggiornato dopo la modifica delle giornate
    // effettuata nel service delle giornate
    @Transactional
    public Calendar updateDay(Long id, List<OpeningDayUpdateRequest> request) {
        Calendar c = getById(id);
        daySvc.update(c.getId(), request);
        return c;
    }


    // per ritornare il calendario aggiornato dopo l'aggiunta di un extra range
    // effettuata nel service degli slot
    @Transactional
    public Calendar addRange(Long id, OpeningDayUpdateRequest request) {
        Calendar c = getById(id);
        daySvc.addRange(c.getId(), request);
        return c;
    }

    // modifica stato attivazione calendario
    public Calendar changeStatus(Long id, boolean isActive) {
        Calendar c = getById(id);
        c.setIsActive(isActive);
        return calendarRepo.save(c);
    }


    // gestione periodi di sospensione del calendario
    public Calendar handleOnHoliday(Long id, HolidayRequest request) {
        Calendar c = getById(id);

        if (request.getHolidayDateStart() != null && request.getHolidayDateEnd() != null) {
            List<Appointment> apps = c.getAppointments().stream().filter(a -> !a.getStatus().equals(AppointmentStatus.CANCELLED) && a.getStartDate().toLocalDate()
                    .isAfter(request.getHolidayDateStart()) && a.getStartDate().toLocalDate().isBefore(request.getHolidayDateEnd())).toList();

            if (apps.size() > 0)
                throw new IllegalArgumentException("Ci sono appuntamenti prenotati per il periodo previsto, sposta gli appuntamenti prima di impostare questo periodo di sospensione");
        }

        c.setOnHoliday(request.getOnHoliday());
        if (!request.getOnHoliday()) {
            c.setHolidayDateStart(null);
            c.setHolidayDateEnd(null);
        } else {
            c.setHolidayDateStart(request.getHolidayDateStart());
            c.setHolidayDateEnd(request.getHolidayDateEnd());
        }
        return calendarRepo.save(c);
    }
}
