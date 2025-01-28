package epicode.it.healthdesk.entities.specialization.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SpecializationRequest {
    @NotNull(message = "Nome specializzazione richiesto")
    private String name;
    private String description;

    private LocalDate date;
}
