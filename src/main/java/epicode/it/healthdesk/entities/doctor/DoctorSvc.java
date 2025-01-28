package epicode.it.healthdesk.entities.doctor;


import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorRequest;
import epicode.it.healthdesk.entities.doctor.dto.DoctorUpdateAddInfoRequest;
import epicode.it.healthdesk.entities.experience.ExperienceSvc;
import epicode.it.healthdesk.entities.payment_method.PaymentMethod;
import epicode.it.healthdesk.entities.payment_method.PaymentMethodSvc;
import epicode.it.healthdesk.entities.service.DoctorServiceSvc;
import epicode.it.healthdesk.entities.specialization.SpecializationSvc;
import epicode.it.healthdesk.entities.training.TrainingSvc;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class DoctorSvc {
    private final DoctorRepo doctorRepo;
    private final DoctorMapper mapper;
    private final SpecializationSvc specializationSvc;
    private final DoctorServiceSvc doctorServiceSvc;
    private final ExperienceSvc experienceSvc;
    private final TrainingSvc trainingSvc;
    private final PaymentMethodSvc paymentMethodSvc;

    public List<Doctor> getAll() {
        return doctorRepo.findAll();
    }

    public Page<Doctor> getAllPageable(Pageable pageable) {
        return doctorRepo.findAll(pageable);
    }

    public Doctor getById(Long id) {
        return doctorRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Medico non trovato"));
    }

    public int count() {
        return (int) doctorRepo.count();
    }

    public String delete(Long id) {
        Doctor e = getById(id);
        doctorRepo.delete(e);
        return "Medico eliminato con successo";
    }

    public String delete(Doctor e) {
        Doctor foundDoctor = getById(e.getId());
        doctorRepo.delete(foundDoctor);
        return "Medico eliminato con successo";
    }

    public Doctor getByEmail(String email) {
        return doctorRepo.findFirstByEmail(email).orElseThrow(() -> new EntityNotFoundException("Medico non trovato"));
    }

    @Transactional
    public Doctor create(@Valid DoctorRequest request) {
        Doctor d = doctorRepo.save(mapper.fromDoctorRequestToDoctor(request));
        Calendar c = new Calendar();
        c.setDoctor(d);
        d.setCalendar(c);
        return d;
    }

    @Transactional
    public Doctor updateAddInfo(Long id, @Valid DoctorUpdateAddInfoRequest request) {
        Doctor d = getById(id);
        if (request.getSpecializations() != null) {
            d.getSpecializations().addAll(specializationSvc.saveAll(d, request.getSpecializations()));
        }
        if (request.getServices() != null) {
            d.getServices().addAll(doctorServiceSvc.saveAll(d, request.getServices()));
        }
        if (request.getExperiences() != null) {
            d.getExperiences().addAll(experienceSvc.saveAll(d, request.getExperiences()));
        }
        if (request.getTrainings() != null) {
            d.getTrainings().addAll(trainingSvc.saveAll(d, request.getTrainings()));
        }
        if (request.getPayments() != null) {
            List<PaymentMethod> paymentMethods = new ArrayList<>();
            request.getPayments().forEach(p -> {
                paymentMethods.add(paymentMethodSvc.getByName(p));
            });
            d.getPaymentMethods().addAll(paymentMethods);
        }
        return doctorRepo.save(d);
    }
}
