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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
public class DoctorRunner implements ApplicationRunner {
    private final AppUserSvc appUserSvc;
    private final DoctorSvc doctorSvc;
    private final Faker faker;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (doctorSvc.count() == 0) {

            RegisterDoctorRequest request = new RegisterDoctorRequest();

            request.setEmail("infohealthdesk@gmail.com");
            request.setPassword("doctorpwd");

            DoctorRequest doctorRequest = new DoctorRequest();
            doctorRequest.setName("Maria Valentina");
            doctorRequest.setSurname("Barone");
            doctorRequest.setLicenceNumber(faker.regexify("[A-Z0-9]{8}"));
            doctorRequest.setPhoneNumber("3298180450");

            AddressRequestForDoctor addressRequest1 = new AddressRequestForDoctor();
            addressRequest1.setStreet("Via Giusti");
            addressRequest1.setStreetNumber("5");
            addressRequest1.setProvinceAcronym("PA");
            addressRequest1.setCity("Palermo");
            addressRequest1.setPostalCode("90144");
            addressRequest1.setName("Palermo");

            AddressRequestForDoctor addressRequest2 = new AddressRequestForDoctor();
            addressRequest2.setStreet("Via Libertà");
            addressRequest2.setStreetNumber("10");
            addressRequest2.setProvinceAcronym("TP");
            addressRequest2.setCity("Castellammare del Golfo");
            addressRequest2.setPostalCode("91014");
            addressRequest2.setName("Castellammare G.fo");

            doctorRequest.getAddresses().addAll(List.of(addressRequest1, addressRequest2));

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
