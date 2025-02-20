package epicode.it.healthdesk.entities.experience.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
public class ExperienceRequest {
    @NotNull(message="Data esperienza curriculare richiesta")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message="Nome esperienza curriculare richiesta")
    private String name;

    private String description;
}
