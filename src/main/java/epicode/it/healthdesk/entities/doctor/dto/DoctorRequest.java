package epicode.it.healthdesk.entities.doctor.dto;

import epicode.it.healthdesk.entities.address.dto.AddressRequestForDoctor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DoctorRequest {
    @NotNull(message = "Nome richiesto")
    private String name;

    @NotNull(message = "Cognome richiesto")
    private String surname;

    private String avatar;

    @Email(message = "Email non valida")
    private String email;

    @NotNull(message = "Numero di telefono richiesto")
    private String phoneNumber;

    @NotNull(message = "Codice abilitazione richiesto")
    @Pattern(regexp = "^[A-Z0-9]{8}$", message = "Il codice di abilitazione deve essere alfanumerico di 8 caratteri.")
    private String licenceNumber;

    @NotNull(message = "E' obbligatorio specificare almeno un indirizzo")
    private List<AddressRequestForDoctor> addresses = new ArrayList<>();
}
