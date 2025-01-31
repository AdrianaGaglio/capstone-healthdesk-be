package epicode.it.healthdesk.entities.address.dto;

import lombok.Data;

@Data
public class AddressResponse {
    private Long id;
    private String street;
    private String streetNumber;
    private String province;
    private String provinceAcronym;
    private String city;
    private String postalCode;
    private String additional;
}
