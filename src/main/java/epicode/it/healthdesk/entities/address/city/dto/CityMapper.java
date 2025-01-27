package epicode.it.healthdesk.entities.address.city.dto;

import epicode.it.healthdesk.entities.address.city.City;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityMapper {
    private ModelMapper mapper = new ModelMapper();

    public CityDTO toCityDTO(CityServerResponse serverResponse) {
        CityDTO response = new CityDTO();
        response.setCode(serverResponse.getCodice());
        response.setName(serverResponse.getNome());
        response.setPostalCode(serverResponse.getCap());
        response.setProvinceAcronym(serverResponse.getProvincia().getSigla());
        return response;
    }

    public List<CityDTO> toCityDTOList(List<CityServerResponse> serverResponses) {
        return serverResponses.stream().map(this::toCityDTO).toList();
    }

    public City toCity(CityDTO cityDTO) {
        return mapper.map(cityDTO, City.class);
    }

    public List<City> toCityList(List<CityDTO> cityDTOs) {
        return cityDTOs.stream().map(this::toCity).toList();
    }
}