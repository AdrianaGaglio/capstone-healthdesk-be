package epicode.it.healthdesk.entities.patient.dto;

import epicode.it.healthdesk.entities.address.dto.AddressRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {

    @NotNull(message = "Nome richiesto")
    private String name;

    @NotNull(message = "Cognome richiesto")
    private String surname;

    private String avatar;

    @Pattern(regexp = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$", message = "Codice fiscale non valido")
    private String taxId;

    @NotNull(message = "Data di nascita richiesta")
    @Past(message = "Data di nascita non valida")
    private LocalDate birthDate;

    @NotNull(message = "Numero di telefono richiesto")
    private String phoneNumber;

    private AddressRequest address;
}
