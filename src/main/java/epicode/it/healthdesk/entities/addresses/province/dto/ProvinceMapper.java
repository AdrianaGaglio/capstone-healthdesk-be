package epicode.it.healthdesk.entities.addresses.province.dto;

import epicode.it.healthdesk.entities.addresses.province.Province;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProvinceMapper {

    private ModelMapper mapper = new ModelMapper();

    public ProvinceDTO toProvinceDTO(ProvinceServerResponse serverResponse) {
        ProvinceDTO response = new ProvinceDTO();
        response.setCode(serverResponse.getCodice());
        response.setName(serverResponse.getNome());
        response.setAcronym(serverResponse.getSigla());
        response.setRegion(serverResponse.getRegione());
        return response;
    }

    public List<ProvinceDTO> toProvinceDTOList(List<ProvinceServerResponse> serverResponses) {
        return serverResponses.stream().map(this::toProvinceDTO).toList();
    }

    public Province toProvince(ProvinceDTO provinceDTO) {
        return mapper.map(provinceDTO, Province.class);
    }

    public List<Province> toProvinceList(List<ProvinceDTO> provinceDTOs) {
        return provinceDTOs.stream().map(this::toProvince).toList();
    }
}
