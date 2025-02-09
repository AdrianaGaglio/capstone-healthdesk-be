package epicode.it.healthdesk.entities.document.document;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentCreateRequest {
   @NotNull(message="File richiesto")
    private String file;
   @NotNull(message = "Descrizione richiesta")
   private String description;
}
