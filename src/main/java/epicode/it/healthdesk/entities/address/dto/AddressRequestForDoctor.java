package epicode.it.healthdesk.entities.address.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressRequestForDoctor {

    @NotNull(message = "Nome indirizzo obbligatorio")
    private String name;

    @NotNull(message = "Indirizzo obbligatorio")
    private String street;

    @NotNull(message = "Numero civico obbligatorio")
    private String streetNumber;

    @NotNull(message = "Provincia obbligatoria")
    private String provinceAcronym;

    @NotNull(message = "Città obbligatoria")
    private String city;

    @NotNull(message = "CAP obbligatorio")
    private String postalCode;
}
