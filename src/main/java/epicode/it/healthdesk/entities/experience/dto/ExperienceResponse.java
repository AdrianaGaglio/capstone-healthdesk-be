package epicode.it.healthdesk.entities.experience.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceResponse {
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private String name;

    private String description;
}
