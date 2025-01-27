package epicode.it.healthdesk.entities.address.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressRequest {
    @NotNull(message = "Indirizzo obbligatorio")
    private String street;

    @NotNull(message = "Numero civico obbligatorio")
    private String streetNumber;

    @NotNull(message = "Provincia obbligatoria")
    private String provinceAcronym;

    @NotNull(message = "Città obbligatoria")
    private String cityName;

    @NotNull(message = "CAP obbligatorio")
    private String postalCode;
}
