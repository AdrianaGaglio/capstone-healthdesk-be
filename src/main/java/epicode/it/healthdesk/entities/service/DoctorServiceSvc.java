package epicode.it.healthdesk.entities.service;

import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.appointment.AppointmentRepo;
import epicode.it.healthdesk.entities.appointment.AppointmentSvc;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.service.dto.DoctorServiceMapper;
import epicode.it.healthdesk.entities.service.dto.DoctorServiceRequest;
import epicode.it.healthdesk.entities.service.dto.DoctorServiceResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class DoctorServiceSvc {
    private final DoctorServiceRepo serviceRepo;
    private final AppointmentRepo appRepo;

    public DoctorService getById(Long id) {
        return serviceRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Prestazione non trovata"));
    }

    public DoctorService create(Doctor d, @Valid DoctorServiceRequest request) {
        DoctorService s = new DoctorService();
        BeanUtils.copyProperties(request, s);
        s.setDoctor(d);
        s.setIsActive(false);
        if(request.getOnline() == null) s.setOnline(false);
        return serviceRepo.save(s);
    }

    public List<DoctorService> saveAll(Doctor d, List<DoctorServiceRequest> requests) {
        List<DoctorService> services = new ArrayList<>();
        requests.forEach(request -> {
            services.add(create(d, request));
        });
        return serviceRepo.saveAll(services);
    }

    public String delete(Long id) {
        if (!serviceRepo.existsById(id)) {
            throw new EntityNotFoundException("Prestazione non trovata");
        }
        DoctorService s = getById(id);
        List<Appointment> appointments = appRepo.findByService(s);
        appointments.forEach(a -> {
            if (a.getStartDate().isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("Impossibile cancellare prestazione: ci sono appuntamenti in programma!");
            }
            a.setService(null);
        });

        serviceRepo.delete(s);
        return "Prestazione eliminata con successo";
    }

    public DoctorService updateAvailability(Long id) {
        if (!serviceRepo.existsById(id)) {
            throw new EntityNotFoundException("Prestazione non trovata");
        }
        DoctorService s = getById(id);
        s.setOnline(!s.getOnline());
        return serviceRepo.save(s);
    }

    public DoctorService updateActivation(Long id) {
        if (!serviceRepo.existsById(id)) {
            throw new EntityNotFoundException("Prestazione non trovata");
        }
        DoctorService s = getById(id);
        s.setIsActive(!s.getIsActive());
        return serviceRepo.save(s);
    }
}
