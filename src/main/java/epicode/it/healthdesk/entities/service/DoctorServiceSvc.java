package epicode.it.healthdesk.entities.service;

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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class DoctorServiceSvc {
    private final DoctorServiceRepo serviceRepo;

    public DoctorService create(@Valid DoctorServiceRequest request) {
        DoctorService s = new DoctorService();
        BeanUtils.copyProperties(request, s);
        return serviceRepo.save(s);
    }

    public List<DoctorService> saveAll(Doctor d, List<DoctorServiceRequest> requests) {
        List<DoctorService> services = new ArrayList<>();
        requests.forEach(request -> {
            DoctorService s = new DoctorService();
            BeanUtils.copyProperties(request, s);
            s.setDoctor(d);
            services.add(s);
        });
        return serviceRepo.saveAll(services);
    }

    public String delete(Long id) {
        if (!serviceRepo.existsById(id)) {
            throw new EntityNotFoundException("Servizio non trovato");
        }
        serviceRepo.deleteById(id);
        return "Servizio eliminato con successo";
    }
}
