package epicode.it.healthdesk.entities.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DoctorServiceRequest {
    @NotNull(message = "Nome del servizio richiesto")
    private String name;

    private String description;

    @NotNull(message = "Costo del servizio richiesto")
    private double price;

    @NotNull(message = "Specificare se il servizio è disponibile online")
    private boolean online;
}
