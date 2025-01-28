package epicode.it.healthdesk.entities.training.dto;

import epicode.it.healthdesk.entities.training.Training;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingMapper {
    private ModelMapper mapper = new ModelMapper();

    public TrainingResponse toTrainingResponse(Training t) {
        return mapper.map(t, TrainingResponse.class);
    }

    public List<TrainingResponse> toTrainingResponseList(List<Training> trainings) {
        return trainings.stream().map(this::toTrainingResponse).toList();
    }
}
