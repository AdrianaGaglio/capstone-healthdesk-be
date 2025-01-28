package epicode.it.healthdesk.entities.specialization;

import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.specialization.dto.SpecializationMapper;
import epicode.it.healthdesk.entities.specialization.dto.SpecializationRequest;
import epicode.it.healthdesk.entities.specialization.dto.SpecializationResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class SpecializationSvc {
    private final SpecializationRepo specializationRepo;
    private final SpecializationMapper mapper;

    public SpecializationResponse create(@Valid SpecializationRequest request) {
        Specialization s = new Specialization();
        BeanUtils.copyProperties(request, s);
        return mapper.toSpecializationResponse(specializationRepo.save(s));
    }

    public List<SpecializationResponse> saveAll(Doctor d, List<SpecializationRequest> requests) {
        List<Specialization> specializations = new ArrayList<>();
        requests.forEach(request -> {
            Specialization s = new Specialization();
            BeanUtils.copyProperties(request, s);
            s.setDoctor(d);
            specializations.add(s);
        });
        return mapper.toSpecializationResponseList(specializationRepo.saveAll(specializations));
    }

    public String delete(Long id) {
        if (!specializationRepo.existsById(id)) {
            throw new EntityNotFoundException("Specializzazione non trovata");
        }
        specializationRepo.deleteById(id);
        return "Specializzazione eliminata con successo";
    }
}
