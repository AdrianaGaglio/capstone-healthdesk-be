package epicode.it.healthdesk.entities.appointment;

import com.github.javafaker.App;
import epicode.it.healthdesk.entities.address.AddressSvc;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentDateUpdate;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentRequest;
import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.calendar.CalendarRepo;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import epicode.it.healthdesk.entities.medial_folder.MedicalFolderSvc;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.patient.PatientSvc;
import epicode.it.healthdesk.entities.service.DoctorService;
import epicode.it.healthdesk.entities.service.DoctorServiceSvc;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class AppointmentSvc {
    private final AppointmentRepo appointmentRepo;
    private final DoctorServiceSvc serviceSvc;
    private final DoctorSvc doctorSvc;
    private final MedicalFolderSvc medicalFolderSvc;
    private final CalendarRepo calendarRepo;
    private final AddressSvc addressSvc;

    public List<Appointment> getAll() {
        return appointmentRepo.findAll();
    }

    public Page<Appointment> getAllPageable(Pageable pageable) {
        return appointmentRepo.findAll(pageable);
    }

    public Appointment getById(Long id) {
        return appointmentRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Appuntamento non trovato"));
    }

    public int count() {
        return (int) appointmentRepo.count();
    }


    public String delete(Long id) {
        Appointment e = getById(id);
        appointmentRepo.delete(e);
        return "Appuntamento eliminato con successo";
    }


    public String delete(Appointment e) {
        Appointment foundAppointment = getById(e.getId());
        appointmentRepo.delete(foundAppointment);
        return "Appuntamento eliminato con successo";
    }

    public Appointment findFirstByCalendarIdAndStartDate(Long calendarId, LocalDateTime startDate) {
        return appointmentRepo.findFirstByCalendarIdAndStartDate(calendarId, startDate).orElse(null);
    }


    // nuovo appuntamento
    @Transactional
    public Appointment create(@Valid AppointmentRequest request) {
        Calendar c = doctorSvc.getById(request.getDoctorId()).getCalendar();
        Doctor d = doctorSvc.getById(request.getDoctorId());

        Appointment app = findFirstByCalendarIdAndStartDate(c.getId(), request.getStartDate());

        // se ci sono appuntamenti in programma che non sono cancellati, lancio l'eccezione
        if (app != null && app.getStatus() != AppointmentStatus.CANCELLED) {
            throw new EntityExistsException("Slot non disponibile");
        }

        // controllo che ora di inizio e fine siano concruenti
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("L'ora di fine precede quella di inizio");
        }

        Appointment a = new Appointment();
        BeanUtils.copyProperties(request, a);
        a.setService(serviceSvc.getById(request.getServiceId())); // servizio prenotato
        a.setCalendar(c); // riferimento all'agenda del medico
        a.setMedicalFolder(medicalFolderSvc.getByPatient(request.getPatientId())); // assegnazione dell'appuntamento alla cartella medica
        if (request.getOnline() == false && request.getDoctorAddressId() == null) {
            // se visita in presenza, lancio eccezione nel caso in cui manchi l'indirizzo
            throw new IllegalArgumentException("Indirizzo del medico richiesto per visita in presenza");
        }

        // imposto se online o meno
        if (request.getOnline() == true) {
            a.setOnline(true);
        } else {
            a.setOnline(false);
        }

        // se c'è un indirizzo per visita in presenza, allora lo assegno all'appuntamento
        if (request.getDoctorAddressId() != null) {
            a.setDoctorAddress(addressSvc.getById(request.getDoctorAddressId()));
        }

        // l'appuntamento appena creato ha uno stato di default pending
        a.setStatus(AppointmentStatus.PENDING);

        // se il paziente non è già stato assegnato al medico, lo assegno
        Patient p = medicalFolderSvc.getByPatient(request.getPatientId()).getPatient();
        if (!d.getPatients().contains(p)) {
            d.getPatients().add(p);
        }

        return appointmentRepo.save(a);
    }

    public List<Appointment> findByService(DoctorService service) {
        return appointmentRepo.findByService(service);
    }

    // per ottenere i prossimi appuntamenti da mostrare al medico
    public Page<Appointment> findByCalendarNext(Long calendarId, Pageable pageable) {
        Calendar c = calendarRepo.findById(calendarId)
                .orElseThrow(() -> new EntityNotFoundException("Agenda non trovata"));


        return appointmentRepo.findByCalendarAndStartDateAfter(
                c,
                LocalDateTime.now(),
                pageable
        );
    }

    public boolean existByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepo.existsByStartDateAndEndDate(startDate, endDate);
    }

    public Appointment findFirstByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepo.findFirstByStartDateAndEndDate(startDate, endDate);
    }

    // modifica appuntamento da parte del medico
    public Calendar update(Long id, Appointment request) {
        Appointment a = getById(id);

        // aggiorno lo stato
        a.setStatus(request.getStatus());

        // controllo se per le date indicate esiste un appuntamento
        if (existByStartDateAndEndDate(request.getStartDate(), request.getEndDate())) {
            Appointment found = findFirstByStartDateAndEndDate(request.getStartDate(), request.getEndDate());
            // se l'appuntamento trovato è diverso da quello che sto modificando, lancio l'eccezione
            if (!found.getId().equals(request.getId()))
                throw new EntityExistsException("Slot non disponibile");
        }

        // altrimenti imposto le nuove date
        a.setStartDate(request.getStartDate());
        a.setEndDate(request.getEndDate());

        appointmentRepo.save(a);
        return calendarRepo.findById(a.getCalendar().getId()).orElse(null);
    }

    // per ottenere l'ultima visita effettuata dal paziente
    public Appointment findLastByMedicalFolder(Long patientId) {
        MedicalFolder mf = medicalFolderSvc.getByPatient(patientId);
        return appointmentRepo.findLastByMedicalFolder(mf.getId()).stream().findFirst().orElse(null);
    }

    // cancellazione appuntamento (modifica stato in CANCELLED) --- per il paziente
    public Appointment cancelApp(Long id) {
        Appointment a = getById(id);
        a.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepo.save(a);
    }

    // modifica date appuntamento --- per il paziente
    public Appointment updateDate(Long id, @Valid AppointmentDateUpdate request) {
        Appointment a = getById(id);

        // controllo se per le date indicate esiste un appuntamento
        if (existByStartDateAndEndDate(request.getStartDate(), request.getEndDate())) {
            Appointment found = findFirstByStartDateAndEndDate(request.getStartDate(), request.getEndDate());
            // se l'appuntamento trovato è diverso da quello che sto modificando, lancio l'eccezione
            if (!found.getId().equals(request.getId()))
                throw new EntityExistsException("Slot non disponibile");
        }

        a.setStartDate(request.getStartDate());
        a.setEndDate(request.getEndDate());
        return appointmentRepo.save(a);
    }

}