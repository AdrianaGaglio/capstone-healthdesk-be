package epicode.it.healthdesk.entities.appointment;

import com.github.javafaker.App;
import epicode.it.healthdesk.entities.address.AddressSvc;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentDateUpdate;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentRequest;
import epicode.it.healthdesk.entities.appointment.dto.BlockedSlotRequest;
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
import epicode.it.healthdesk.utilities.email.EmailMapper;
import epicode.it.healthdesk.utilities.email.EmailRequest;
import epicode.it.healthdesk.utilities.email.EmailSvc;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
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
    private final EmailMapper emailMapper;
    private final EmailSvc emailSvc;

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

    public Appointment blockedSlot(BlockedSlotRequest request) {
        Doctor d = doctorSvc.getById(request.getDoctorId());
        Calendar c = d.getCalendar();

        Appointment app = findFirstByCalendarIdAndStartDate(c.getId(), request.getStartDate());
        if (app != null && app.getStatus() != AppointmentStatus.CANCELLED) {
            throw new EntityExistsException("Slot già prenotato");
        }

        // controllo che ora di inizio e fine siano concruenti
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("L'ora di fine precede quella di inizio");
        }

        if(request.getStartDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Non è possibile bloccare uno slot nel passato");
        }

        Appointment a = new Appointment();
        BeanUtils.copyProperties(request, a);
        a.setService(null); // servizio prenotato
        a.setCalendar(c); // riferimento all'agenda del medico
        a.setMedicalFolder(null);
        a.setOnline(null);
        a.setDoctorAddress(null);
        a.setStatus(AppointmentStatus.BLOCKED);
        return appointmentRepo.save(a);
    }

    // nuovo appuntamento
    @Transactional
    public Appointment create(@Valid AppointmentRequest request, UserDetails userDetails) {

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

        if(request.getStartDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Non é possibile prenotare un appuntamento nel passato");
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

        a = appointmentRepo.save(a);

        EmailRequest mailForDoctor = new EmailRequest();
        EmailRequest mailForPatient = new EmailRequest();
        String subject = "Health Desk - Nuovo appuntamento prenotato";
        mailForDoctor.setSubject(subject);
        mailForPatient.setSubject(subject);

        if (userDetails == null || userDetails.getAuthorities().stream().anyMatch(auth -> !auth.getAuthority().equals("ROLE_DOCTOR"))) {
            mailForDoctor.setTo(a.getCalendar().getDoctor().getAppUser().getEmail());
            mailForDoctor.setBody(emailMapper.toNewAppointment(a, true));
            mailForPatient.setTo(a.getMedicalFolder().getPatient().getAppUser().getEmail());
            mailForPatient.setBody(emailMapper.toNewAppointment(a, false));
            emailSvc.sendEmailHtml(mailForDoctor);
            emailSvc.sendEmailHtml(mailForPatient);
        } else {
            mailForPatient.setTo(a.getMedicalFolder().getPatient().getAppUser().getEmail());
            mailForPatient.setBody(emailMapper.toNewAppointment(a, false));
            emailSvc.sendEmailHtml(mailForPatient);
        }

        return a;
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

    public List<Appointment> findAllByStartDateBetween(LocalDate start, LocalDate end) {
        return appointmentRepo.findAllByStartDateBetween(start, end);
    }

    // modifica appuntamento da parte del medico
    public Calendar update(Long id, Appointment request) {
        Appointment a = getById(id);

        EmailRequest cancellationEmail = new EmailRequest();
        cancellationEmail.setTo(a.getMedicalFolder().getPatient().getAppUser().getEmail());
        cancellationEmail.setSubject("Health Desk - Conferma cancellazione appuntamento");

        // aggiorno lo stato
        if (!a.getStatus().equals(request.getStatus())) {
            a.setStatus(request.getStatus());
        }

        // controllo se per le date indicate esiste un appuntamento
        if (existByStartDateAndEndDate(request.getStartDate(), request.getEndDate())) {
            Appointment found = findFirstByStartDateAndEndDate(request.getStartDate(), request.getEndDate());
            // se l'appuntamento trovato è diverso da quello che sto modificando, lancio l'eccezione
            if (!found.getId().equals(request.getId()))
                throw new EntityExistsException("Slot non disponibile");
        }

        EmailRequest changeDateEmail = new EmailRequest();
        changeDateEmail.setTo(a.getMedicalFolder().getPatient().getAppUser().getEmail());
        changeDateEmail.setSubject("Health Desk - Modifica data appuntamento");


        if(!a.getStartDate().equals(request.getStartDate())) {
            // imposto le nuove date
            a.setStartDate(request.getStartDate());
            a.setEndDate(request.getEndDate());
            changeDateEmail.setBody(emailMapper.toAppointmentDateChange(a, false));
            emailSvc.sendEmailHtml(changeDateEmail);
        }

        appointmentRepo.save(a);

        if (a.getStatus().equals(AppointmentStatus.CANCELLED)) {
            cancellationEmail.setBody(emailMapper.toAppCancellationForUser(a));
            emailSvc.sendEmailHtml(cancellationEmail);
        }

        return calendarRepo.findById(a.getCalendar().getId()).orElse(null);
    }

    // per ottenere l'ultima visita effettuata dal paziente
    public Appointment findLastByMedicalFolder(Long patientId) {
        MedicalFolder mf = medicalFolderSvc.getByPatient(patientId);
        return appointmentRepo.findLastByMedicalFolder(mf.getId()).stream().findFirst().orElse(null);
    }

    // cancellazione appuntamento (modifica stato in CANCELLED) --- per il paziente / medico
    public Appointment cancelApp(Long id, UserDetails userDetails) {
        Appointment a = getById(id);
        a.setStatus(AppointmentStatus.CANCELLED);

        EmailRequest mailForDoctor = new EmailRequest();
        EmailRequest mailForPatient = new EmailRequest();
        String subject = "Health Desk - Cancellazione appuntamento";
        mailForDoctor.setSubject(subject);
        mailForPatient.setSubject(subject);

        if(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_DOCTOR"))) {
            mailForPatient.setTo(a.getMedicalFolder().getPatient().getAppUser().getEmail());
            mailForPatient.setBody(emailMapper.toAppCancellationForUser(a));
            emailSvc.sendEmailHtml(mailForPatient);
        } else {
            mailForDoctor.setTo(a.getCalendar().getDoctor().getAppUser().getEmail());
            mailForDoctor.setBody(emailMapper.toAppointmentStatusChange(a, "cancellato"));
            mailForPatient.setTo(a.getMedicalFolder().getPatient().getAppUser().getEmail());
            mailForPatient.setBody(emailMapper.toAppCancellationForUser(a));
            emailSvc.sendEmailHtml(mailForPatient);
            emailSvc.sendEmailHtml(mailForDoctor);
        }

        return appointmentRepo.save(a);
    }

    // conferma appuntamento (modifica stato in CANCELLED) --- per il paziente / medico
    public Appointment confirmApp(Long id, UserDetails userDetails) {
        Appointment a = getById(id);
        a.setStatus(AppointmentStatus.CONFIRMED);

        if(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_PATIENT"))) {
            EmailRequest mail = new EmailRequest();
            mail.setTo(a.getCalendar().getDoctor().getAppUser().getEmail());
            mail.setSubject("Health Desk - Conferma appuntamento" );
            mail.setBody(emailMapper.toAppointmentStatusChange(a, "confermato"));
            emailSvc.sendEmailHtml(mail);
        }
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

        EmailRequest mailForDoctor = new EmailRequest();
        mailForDoctor.setTo(a.getCalendar().getDoctor().getAppUser().getEmail());
        mailForDoctor.setSubject("Health Desk - Modifica data appuntamento");
        mailForDoctor.setBody(emailMapper.toAppointmentDateChange(a, true));
        emailSvc.sendEmailHtml(mailForDoctor);

        EmailRequest mailForPatient = new EmailRequest();
        mailForPatient.setTo(a.getMedicalFolder().getPatient().getAppUser().getEmail());
        mailForPatient.setSubject("Health Desk - Modifica data appuntamento");
        mailForPatient.setBody(emailMapper.toAppointmentDateChange(a, false));
        emailSvc.sendEmailHtml(mailForPatient);

        return appointmentRepo.save(a);
    }

}