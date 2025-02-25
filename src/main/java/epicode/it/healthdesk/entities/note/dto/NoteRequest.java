package epicode.it.healthdesk.entities.note.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NoteRequest {
    @NotNull(message = "Titolo nota richiesto")
    private String title;
    @NotNull(message = "Descrizione nota richiesta")
    private String description;
    private LocalDate date;
}
