package epicode.it.healthdesk.entities.experience.dto;

import epicode.it.healthdesk.entities.experience.Experience;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExperienceMapper {
    private ModelMapper mapper = new ModelMapper();

    public ExperienceResponse toExperienceResponse(Experience e) {
        return mapper.map(e, ExperienceResponse.class);
    }

    public List<ExperienceResponse> toExperienceResponseList(List<Experience> experiences) {
        return experiences.stream().map(this::toExperienceResponse).toList();
    }
}
