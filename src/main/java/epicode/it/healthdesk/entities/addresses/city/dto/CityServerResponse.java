package epicode.it.healthdesk.entities.addresses.city.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import epicode.it.healthdesk.entities.addresses.province.dto.ProvinceServerResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityServerResponse {
    private String codice;
    private String nome;
    private String cap;
    private ProvinceServerResponse provincia;
}
