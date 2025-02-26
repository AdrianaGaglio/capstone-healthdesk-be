package epicode.it.healthdesk.entities.address.dto;

import epicode.it.healthdesk.entities.address.Address;
import epicode.it.healthdesk.entities.address.city.CitySvc;
import epicode.it.healthdesk.entities.address.province.ProvinceSvc;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AddressMapper {
    private ModelMapper mapper = new ModelMapper();

    public Address toAddress(AddressRequest request) {
        Address response = mapper.map(request, Address.class);
        return response;
    }

    public AddressResponse fromAddressToAddressResponse(Address address) {
        AddressResponse response = mapper.map(address, AddressResponse.class);
        return response;
    }

    public List<AddressResponse> fromAddressToAddressResponseList(List<Address> addresses) {
        return addresses.stream().map(this::fromAddressToAddressResponse).toList();
    }
}
