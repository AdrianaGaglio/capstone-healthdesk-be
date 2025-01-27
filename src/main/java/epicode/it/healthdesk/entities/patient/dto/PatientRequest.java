package epicode.it.healthdesk.entities.patient.dto;

import epicode.it.healthdesk.entities.addresses.dto.AddressRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PatientRequest {

    @NotNull(message = "Nome richiesto")
    private String name;

    @NotNull(message = "Cognome richiesto")
    private String surname;

    private String avatar;

    @NotNull(message = "Codice fiscale richiesto")
    @Pattern(regexp = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$", message="Codice fiscale non valido")
    private String taxId;

    @NotNull(message = "Numero di telefono richiesto")
    private String phoneNumber;

    @NotNull(message = "Indirizzo richiesto")
    private AddressRequest address;
}
