package epicode.it.healthdesk.entities.experience;

import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.experience.dto.ExperienceMapper;
import epicode.it.healthdesk.entities.experience.dto.ExperienceRequest;
import epicode.it.healthdesk.entities.experience.dto.ExperienceResponse;
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
public class ExperienceSvc {
    private final ExperienceRepo experienceRepo;

    public Experience create(@Valid ExperienceRequest request) {
        Experience e = new Experience();
        BeanUtils.copyProperties(request, e);
        return experienceRepo.save(e);
    }

    public List<Experience> saveAll(Doctor d, List<ExperienceRequest> requests) {
        List<Experience> experiences = new ArrayList<>();
        requests.forEach(request -> {
            Experience e = new Experience();
            BeanUtils.copyProperties(request, e);
            e.setDoctor(d);
            experiences.add(e);
        });
        return experienceRepo.saveAll(experiences);
    }

    public String delete(Long id) {
        if (!experienceRepo.existsById(id)) {
            throw new EntityNotFoundException("Esperienza curriculare non trovata");
        }
        experienceRepo.deleteById(id);
        return "Esperienza curriculare eliminata con successo";
    }
}
