package epicode.it.healthdesk.entities.patient;

import com.github.javafaker.Faker;
import epicode.it.healthdesk.auth.appuser.AppUserSvc;
import epicode.it.healthdesk.auth.dto.RegisterRequest;
import epicode.it.healthdesk.entities.address.city.City;
import epicode.it.healthdesk.entities.address.city.CitySvc;
import epicode.it.healthdesk.entities.address.city.dto.CityDTO;
import epicode.it.healthdesk.entities.address.dto.AddressRequest;
import epicode.it.healthdesk.entities.address.province.Province;
import epicode.it.healthdesk.entities.address.province.ProvinceSvc;
import epicode.it.healthdesk.entities.address.province.dto.ProvinceDTO;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.patient.dto.PatientRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Component
@Order(5)
@RequiredArgsConstructor
public class PatientRunner implements ApplicationRunner {
    private final AppUserSvc appUserSvc;
    private final PatientSvc patientSvc;
    private final ProvinceSvc provinceSvc;
    private final CitySvc citySvc;
    private final Faker faker;


    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (patientSvc.count() > 0) return;
        List<ProvinceDTO> provinces = provinceSvc.getProvinces();

        if (provinces.size() > 0) {
            for (int i = 0; i < 50; i++) {
                RegisterRequest request = new RegisterRequest();

                request.setPassword("password");
                PatientRequest patientRequest = new PatientRequest();
                patientRequest.setName(faker.name().firstName());
                patientRequest.setSurname(faker.name().lastName());
                patientRequest.setTaxId(faker.regexify("[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]"));
                patientRequest.setBirthDate(faker.date().past(18250, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                patientRequest.setPhoneNumber(faker.phoneNumber().phoneNumber());

                String surname = patientRequest.getSurname();
                surname = surname.replace("'", "");
                surname = surname.replace(" ", "");
                surname = surname.trim();

                request.setEmail(patientRequest.getName().toLowerCase() + "." + surname.toLowerCase() + "@mail.com");

                ProvinceDTO p = provinces.get(faker.random().nextInt(provinces.size()));
                List<CityDTO> cities = citySvc.findByProvince(p.getName());

                if (cities.size() > 0) {
                    CityDTO city = cities.get(faker.random().nextInt(citySvc.findByProvince(p.getName()).size()));

                    AddressRequest addressRequest = new AddressRequest();
                    addressRequest.setStreet(faker.address().streetName());
                    addressRequest.setStreetNumber(faker.address().streetAddressNumber());
                    addressRequest.setProvinceAcronym(p.getAcronym());
                    addressRequest.setCity(city.getName());
                    addressRequest.setPostalCode(city.getPostalCode());
                    patientRequest.setAddress(addressRequest);
                    request.setPatient(patientRequest);

                    try {
                        appUserSvc.registerPatient(request);
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                        System.out.println(request);
                        throw new RuntimeException(e);
                    }
                }
            }

        }
    }
}
