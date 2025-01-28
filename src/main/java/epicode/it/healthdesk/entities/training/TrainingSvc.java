package epicode.it.healthdesk.entities.training;

import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.training.dto.TrainingMapper;
import epicode.it.healthdesk.entities.training.dto.TrainingRequest;
import epicode.it.healthdesk.entities.training.dto.TrainingResponse;
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
public class TrainingSvc {
    private final TrainingRepo trainingRepo;


    public Training create(@Valid TrainingRequest request) {
        Training t = new Training();
        BeanUtils.copyProperties(request, t);
        return trainingRepo.save(t);
    }

    public List<Training> saveAll(Doctor d, List<TrainingRequest> requests) {
        List<Training> trainings = new ArrayList<>();
        requests.forEach(request -> {
            Training t = new Training();
            BeanUtils.copyProperties(request, t);
            trainings.add(t);
        });
        return trainingRepo.saveAll(trainings);
    }

    public String delete(Long id) {
        if (!trainingRepo.existsById(id)) {
            throw new EntityNotFoundException("Esperienza formativa non trovata");
        }
        trainingRepo.deleteById(id);
        return "Esperienza formativa eliminata con successo";
    }
}
