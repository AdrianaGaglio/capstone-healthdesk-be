package epicode.it.healthdesk.entities.address.city.dto;

import lombok.Data;

@Data
public class CityDTO {
    private String name;
    private String code;
    private String postalCode;
    private String provinceAcronym;
}
