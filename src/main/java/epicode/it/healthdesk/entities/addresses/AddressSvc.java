package epicode.it.healthdesk.entities.addresses;

import epicode.it.healthdesk.entities.addresses.city.City;
import epicode.it.healthdesk.entities.addresses.city.CitySvc;
import epicode.it.healthdesk.entities.addresses.dto.AddressMapper;
import epicode.it.healthdesk.entities.addresses.dto.AddressRequest;
import epicode.it.healthdesk.exceptions.AddressMismatchingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AddressSvc {
    private final AddressRepo addressRepo;
    private final CitySvc citySvc;
    private final AddressMapper mapper;

    public Address create(@Valid AddressRequest request) {
        City city = citySvc.findByNameAndPostalCode(request.getCityName(), request.getPostalCode());
        List<City> cities = citySvc.findByProvinceAcronym(request.getProvinceAcronym());

        if (!cities.contains(city)) {
            throw new AddressMismatchingException("La città e la provincia non corrispondono");
        }

        if (!city.getPostalCode().equals(request.getPostalCode())) {
            throw new AddressMismatchingException("La città e il cap non corrispondono");
        }

        return addressRepo.save(mapper.toAddress(request));
    }

}
