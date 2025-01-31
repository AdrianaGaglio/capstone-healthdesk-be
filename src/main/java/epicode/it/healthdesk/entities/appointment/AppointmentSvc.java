package epicode.it.healthdesk.entities.appointment;

import epicode.it.healthdesk.entities.address.AddressSvc;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentRequest;
import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.medial_folder.MedicalFolderSvc;
import epicode.it.healthdesk.entities.patient.PatientSvc;
import epicode.it.healthdesk.entities.service.DoctorServiceSvc;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AppointmentSvc {
    private final AppointmentRepo appointmentRepo;
    private final DoctorServiceSvc serviceSvc;
    private final DoctorSvc doctorSvc;
    private final MedicalFolderSvc medicalFolderSvc;
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

    @Transactional
    public Appointment create(@Valid AppointmentRequest request) {
        Calendar c = doctorSvc.getById(request.getDoctorId()).getCalendar();

        if (findFirstByCalendarIdAndStartDate(c.getId(), request.getStartDate()) != null) {
            throw new EntityExistsException("Slot non disponibile");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("L'ora di fine precede quella di inizio");
        }

        Appointment a = new Appointment();
        BeanUtils.copyProperties(request, a);
        a.setService(serviceSvc.getById(request.getServiceId()));
        a.setCalendar(c);
        a.setMedicalFolder(medicalFolderSvc.getByPatient(request.getPatientId()));
        if (request.getOnline() == false && request.getDoctorAddressId() == null) {
            throw new IllegalArgumentException("Indirizzo del medico richiesto per visita in presenza");
        }

        if (request.getOnline() == true) {
            a.setOnline(true);
        } else {
            a.setOnline(false);
        }

        if (request.getDoctorAddressId() != null) {
            a.setDoctorAddress(addressSvc.getById(request.getDoctorAddressId()));
        }



        a.setStatus(AppointmentStatus.PENDING);
        return appointmentRepo.save(a);
    }

}