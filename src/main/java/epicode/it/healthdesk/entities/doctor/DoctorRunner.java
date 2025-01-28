package epicode.it.healthdesk.entities.doctor;

import com.github.javafaker.Faker;
import epicode.it.healthdesk.entities.address.city.City;
import epicode.it.healthdesk.entities.address.city.CitySvc;
import epicode.it.healthdesk.entities.address.dto.AddressRequest;
import epicode.it.healthdesk.entities.address.dto.AddressRequestForDoctor;
import epicode.it.healthdesk.entities.address.province.Province;
import epicode.it.healthdesk.entities.address.province.ProvinceSvc;
import epicode.it.healthdesk.entities.doctor.dto.DoctorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DoctorRunner implements ApplicationRunner {
    private final DoctorSvc doctorSvc;
    private final CitySvc citySvc;
    private final ProvinceSvc proviceSvc;
    private final Faker faker;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        DoctorRequest request = new DoctorRequest();
        request.setName(faker.name().firstName());
        request.setSurname(faker.name().lastName());
        request.setLicenceNumber(faker.regexify("[A-Z0-9]{8}"));

        Province p = proviceSvc.findAll().get(faker.random().nextInt(proviceSvc.count()));
        List<City> cities = citySvc.findByProvinceAcronym(p.getAcronym());
        for (int i = 1; i < 3; i++) {
            AddressRequestForDoctor addressRequest = new AddressRequestForDoctor();
            addressRequest.setStreet(faker.address().streetAddress());
            addressRequest.setStreetNumber(faker.address().streetAddressNumber());
            addressRequest.setProvinceAcronym(p.getAcronym());
            City c = cities.get(faker.random().nextInt(cities.size()));
            addressRequest.setCityName(c.getName());
            addressRequest.setPostalCode(c.getPostalCode());
            addressRequest.setName("Studio " + i);
            request.getAddresses().add(addressRequest);
        }

        System.out.println(request);


    }
}
