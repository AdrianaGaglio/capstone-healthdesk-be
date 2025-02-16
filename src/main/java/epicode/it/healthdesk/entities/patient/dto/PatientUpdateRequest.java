package epicode.it.healthdesk.entities.patient.dto;

import epicode.it.healthdesk.entities.address.Address;
import epicode.it.healthdesk.entities.address.dto.AddressRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PatientUpdateRequest {
    @NotNull(message = "ID paziente richiesto")
    private Long id;

    @Pattern(regexp = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$", message = "Codice fiscale non valido")
    private String taxId;

    @Pattern(regexp = "^\\+?\\d{6,15}$", message = "Numero di telefono non valido")
    private String phoneNumber;
    private String avatar;
    private Address address;
}
