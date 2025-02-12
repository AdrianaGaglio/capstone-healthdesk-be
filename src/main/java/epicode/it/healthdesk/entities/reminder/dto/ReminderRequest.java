package epicode.it.healthdesk.entities.reminder.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReminderRequest {
    @NotNull(message = "Frequenza richiesta")
    private String frequency;

    @NotNull(message="Descrizione reminder richiesto")
    private String description;

    @NotNull(message = "Data di inizio richiesta")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message =  "Cartella medica richiesta")
    private Long medicalFolderId;
}
