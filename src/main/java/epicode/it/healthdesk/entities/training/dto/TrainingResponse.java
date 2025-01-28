package epicode.it.healthdesk.entities.training.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainingResponse {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private String name;

    private String description;
}
