package epicode.it.healthdesk.entities.specialization.dto;

import epicode.it.healthdesk.entities.specialization.Specialization;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpecializationMapper {
    private ModelMapper mapper = new ModelMapper();

    public SpecializationResponse toSpecializationResponse(Specialization s) {
        return mapper.map(s, SpecializationResponse.class);
    }

    public List<SpecializationResponse> toSpecializationResponseList(List<Specialization> specializations) {
        return specializations.stream().map(this::toSpecializationResponse).toList();
    }
}
