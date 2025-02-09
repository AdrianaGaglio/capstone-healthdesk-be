//package epicode.it.healthdesk.entities.patient;
//
//import com.github.javafaker.Faker;
//import epicode.it.healthdesk.auth.appuser.AppUserSvc;
//import epicode.it.healthdesk.auth.dto.RegisterRequest;
//import epicode.it.healthdesk.entities.address.city.City;
//import epicode.it.healthdesk.entities.address.city.CitySvc;
//import epicode.it.healthdesk.entities.address.dto.AddressRequest;
//import epicode.it.healthdesk.entities.address.province.ProvinceSvc;
//import epicode.it.healthdesk.entities.patient.dto.PatientRequest;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@Order(5)
//@RequiredArgsConstructor
//public class PatientRunner implements ApplicationRunner {
//    private final AppUserSvc appUserSvc;
//    private final Faker faker;
//
//    @Transactional
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//
//        RegisterRequest request = new RegisterRequest();
//
//        request.setPassword("password");
//
//        PatientRequest patientRequest = new PatientRequest();
//        patientRequest.setName(faker.name().firstName());
//        patientRequest.setSurname(faker.name().lastName());
//        patientRequest.setTaxId(faker.regexify("[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]"));
//        patientRequest.setBirthDate(faker.date().past(18250, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//        patientRequest.setPhoneNumber(faker.phoneNumber().phoneNumber());
//
//        String surname = patientRequest.getSurname();
//        if (surname.contains("'")) surname.replace("'", "");
//        if (surname.contains(" ")) surname.replace(" ", "");
//
//        request.setEmail(patientRequest.getName().toLowerCase() + "." + surname.toLowerCase() + "@mail.com");
//
//        AddressRequest addressRequest = new AddressRequest();
//        addressRequest.setStreet(faker.address().streetAddress());
//        addressRequest.setStreetNumber(faker.address().streetAddressNumber());
//        addressRequest.setProvinceAcronym(faker.address().cityPrefix());
//        addressRequest.setCityName(faker.address().cityName());
//        addressRequest.setPostalCode(faker.address().zipCode());
//
//        patientRequest.setAddress(addressRequest);
//
//        request.setPatient(patientRequest);
//
//        try {
//            appUserSvc.registerPatient(request);
//        } catch (RuntimeException e) {
//            System.out.println(e.getMessage());
//            System.out.println(request);
//        }
//
//    }
//}
