package epicode.it.healthdesk.entities.doctor;


import epicode.it.healthdesk.auth.appuser.AppUser;
import epicode.it.healthdesk.auth.jwt.JwtTokenUtil;
import epicode.it.healthdesk.entities.address.Address;
import epicode.it.healthdesk.entities.address.AddressSvc;
import epicode.it.healthdesk.entities.address.dto.AddressRequest;
import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.calendar.CalendarSvc;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorRequest;
import epicode.it.healthdesk.entities.doctor.dto.DoctorUpdateAddInfoRequest;
import epicode.it.healthdesk.entities.doctor.dto.DoctorUpdateRequest;
import epicode.it.healthdesk.entities.experience.ExperienceSvc;
import epicode.it.healthdesk.entities.payment_method.PaymentMethod;
import epicode.it.healthdesk.entities.payment_method.PaymentMethodSvc;
import epicode.it.healthdesk.entities.service.DoctorService;
import epicode.it.healthdesk.entities.service.DoctorServiceSvc;
import epicode.it.healthdesk.entities.specialization.SpecializationSvc;
import epicode.it.healthdesk.entities.training.TrainingSvc;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
    private final CalendarSvc calendarSvc;
    private final AddressSvc addressSvc;

    public List<Doctor> getAll() {
        return doctorRepo.findAll();
    }

    // per il momento non serve
    public Page<Doctor> getAllPageable(Pageable pageable) {
        return doctorRepo.findAll(pageable);
    }

    public Doctor getById(Long id) {
        return doctorRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Medico non trovato"));
    }

    public Doctor save(Doctor d ){
        return doctorRepo.save(d);
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

    // ricerca per email contenuta in appUser (definito metodo nel repo)
    public Doctor getByEmail(String email) {
        return doctorRepo.findFirstByEmail(email).orElse(null);
    }

    // creazione nuovo medico con assegnazione del calendario
    @Transactional
    public Doctor create(@Valid DoctorRequest request) {
        Doctor d = doctorRepo.save(mapper.fromDoctorRequestToDoctor(request));
        d.setCalendar(calendarSvc.create(d));
        return d;
    }

    // modifica informazioni aggiuntive (servizi, specializzazioni, esperienze e formazione)
    // le funzioni vengono eseguite nei rispettivi service
    // ma ritorno il medico aggiornato
    @Transactional
    public Doctor updateAddInfo(Long id, @Valid DoctorUpdateAddInfoRequest request) {
        Doctor d = getById(id);
        if (d == null) throw new EntityNotFoundException("Medico non trovato");
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
        if (request.getAddresses() != null) {
            request.getAddresses().forEach(a -> {
                AddressRequest ar = new AddressRequest();
                BeanUtils.copyProperties(a, ar);
                d.getAddresses().put(a.getName(), addressSvc.create(ar));
            });
        }
        return doctorRepo.save(d);
    }

    // ritorno il medico aggiornato dopo la modifica della disponibilità online/in presenza dei servizi
    public Doctor updateServiceAvailability(Long id, Long serviceId) {
        doctorServiceSvc.updateAvailability(serviceId);
        return getById(id);
    }

    // ritorno il medico aggiornato dopo la modifica dello stato dei servizi
    public Doctor updateServiceActivation(Long id, Long serviceId) {
        doctorServiceSvc.updateActivation(serviceId);
        return getById(id);
    }

    // ritorno il medico aggiornato

    // dopo la cancellazione di un servizio
    public Doctor deleteService(Long id, Long serviceId) {
        doctorServiceSvc.delete(serviceId);
        return getById(id);
    }

    // dopo la cancellazione di una formazione
    public Doctor deleteTraining(Long id, Long trainingId) {
        trainingSvc.delete(trainingId);
        return getById(id);
    }

    // dopo la cancellazione di un'esperienza
    public Doctor deleteExperience(Long id, Long experienceId) {
        experienceSvc.delete(experienceId);
        return getById(id);
    }

    // dopo la cancellazione di una specializzazione
    public Doctor deleteSpecialization(Long id, Long specializationId) {
        specializationSvc.delete(specializationId);
        return getById(id);
    }

    // controllo se esiste un codice licenza
    public boolean existsByLicenceNumber(String licenceNumber) {
        return doctorRepo.existsByLicenceNumber(licenceNumber);
    }

    public Doctor findFirstByLicenceNumber(String licenceNumber) {
        return doctorRepo.findFirstByLicenceNumber(licenceNumber).orElse(null);
    }

    // modifica informazioni personali del medico
    @Transactional
    public Doctor updatePersonalInfo(Long id, @Valid DoctorUpdateRequest request) {
        Doctor d = getById(id);
        BeanUtils.copyProperties(request, d);

        // se viene cambiato il codice di licenza, controllo che non ne esiste uno uguale
        if (existsByLicenceNumber(request.getLicenceNumber()) && findFirstByLicenceNumber(request.getLicenceNumber()).getId() != id) {
            throw new IllegalArgumentException("Codice licenza già utilizzato");
        }
        return doctorRepo.save(d);
    }
}
