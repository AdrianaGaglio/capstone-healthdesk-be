package epicode.it.healthdesk.entities.addresses.city.dto;

import epicode.it.healthdesk.entities.addresses.province.Province;
import lombok.Data;

@Data
public class CityDTO {
    private String name;
    private String code;
    private String postalCode;
    private String provinceAcronym;
}
