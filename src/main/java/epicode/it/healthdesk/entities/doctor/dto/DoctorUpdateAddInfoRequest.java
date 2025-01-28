package epicode.it.healthdesk.entities.doctor.dto;

import epicode.it.healthdesk.entities.experience.dto.ExperienceRequest;
import epicode.it.healthdesk.entities.service.dto.DoctorServiceRequest;
import epicode.it.healthdesk.entities.specialization.dto.SpecializationRequest;
import epicode.it.healthdesk.entities.training.dto.TrainingRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DoctorUpdateAddInfoRequest {
    @NotNull(message = "ID medico richiesto")
    private Long id;
    private List<SpecializationRequest> specializations = new ArrayList<>();
    private List<DoctorServiceRequest> services = new ArrayList<>();
    private List<ExperienceRequest> experiences = new ArrayList<>();
    private List<TrainingRequest> trainings = new ArrayList<>();
    private List<String> payments = new ArrayList<>();
}
