package epicode.it.healthdesk.entities.address;

import epicode.it.healthdesk.entities.address.city.City;
import epicode.it.healthdesk.entities.address.city.CitySvc;
import epicode.it.healthdesk.entities.address.dto.AddressMapper;
import epicode.it.healthdesk.entities.address.dto.AddressRequest;
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
            throw new IllegalArgumentException("La città e la provincia non corrispondono");
        }

        if (!city.getPostalCode().equals(request.getPostalCode())) {
            throw new IllegalArgumentException("La città e il cap non corrispondono");
        }

        return addressRepo.save(mapper.toAddress(request));
    }

    public Address getById(Long id) {
        return addressRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Indirizzo non trovato"));
    }

}
