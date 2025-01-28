package epicode.it.healthdesk.entities.training.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainingRequest {
    @NotNull(message = "Data esperienza formativa richiesta")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Nome esperienza formativa richiesto")
    private String name;

    private String description;

}
