package epicode.it.healthdesk.entities.doctor;

import com.github.javafaker.Faker;
import epicode.it.healthdesk.auth.appuser.AppUser;
import epicode.it.healthdesk.auth.appuser.AppUserSvc;
import epicode.it.healthdesk.auth.dto.RegisterDoctorRequest;
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
    private final AppUserSvc appUserSvc;
    private final DoctorSvc doctorSvc;
    private final CitySvc citySvc;
    private final ProvinceSvc proviceSvc;
    private final Faker faker;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (doctorSvc.count() == 0) {

            RegisterDoctorRequest request = new RegisterDoctorRequest();

            request.setEmail("doctor@mail.com");
            request.setPassword("doctorpwd");

            DoctorRequest doctorRequest = new DoctorRequest();
            doctorRequest.setName(faker.name().firstName());
            doctorRequest.setSurname(faker.name().lastName());
            doctorRequest.setLicenceNumber(faker.regexify("[A-Z0-9]{8}"));
            doctorRequest.setPhoneNumber(faker.phoneNumber().phoneNumber());

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
                doctorRequest.getAddresses().add(addressRequest);
            }

            request.setDoctor(doctorRequest);

            try {
                appUserSvc.registerDoctor(request);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                System.out.println(request);
            }
        }


    }
}
