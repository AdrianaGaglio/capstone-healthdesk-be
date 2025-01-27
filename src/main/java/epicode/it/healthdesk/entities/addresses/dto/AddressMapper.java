package epicode.it.healthdesk.entities.addresses.dto;

import epicode.it.healthdesk.entities.addresses.Address;
import epicode.it.healthdesk.entities.addresses.city.CitySvc;
import epicode.it.healthdesk.entities.addresses.province.ProvinceSvc;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    private ModelMapper mapper = new ModelMapper();

    @Autowired
    private CitySvc citySvc;

    @Autowired
    private ProvinceSvc provinceSvc;

    public Address toAddress(AddressRequest request) {
        Address response = mapper.map(request, Address.class);
        response.setCity(citySvc.findByNameAndPostalCode(request.getCityName(), request.getPostalCode()));
        response.setProvince(provinceSvc.getByAcronym(request.getProvinceAcronym()));
        return response;
    }
}
