//package epicode.it.healthdesk.entities.doctor;
//
//import com.github.javafaker.Faker;
//import epicode.it.healthdesk.auth.appuser.AppUser;
//import epicode.it.healthdesk.auth.appuser.AppUserSvc;
//import epicode.it.healthdesk.auth.dto.RegisterDoctorRequest;
//import epicode.it.healthdesk.entities.address.city.City;
//import epicode.it.healthdesk.entities.address.city.CitySvc;
//import epicode.it.healthdesk.entities.address.dto.AddressRequest;
//import epicode.it.healthdesk.entities.address.dto.AddressRequestForDoctor;
//import epicode.it.healthdesk.entities.address.province.Province;
//import epicode.it.healthdesk.entities.address.province.ProvinceSvc;
//import epicode.it.healthdesk.entities.doctor.dto.DoctorRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@Order(1)
//public class DoctorRunner implements ApplicationRunner {
//    private final AppUserSvc appUserSvc;
//    private final DoctorSvc doctorSvc;
//    private final Faker faker;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//
//        if (doctorSvc.count() == 0) {
//
//            RegisterDoctorRequest request = new RegisterDoctorRequest();
//
//            request.setEmail("infohealthdesk@gmail.com");
//            request.setPassword("doctorpwd");
//
//            DoctorRequest doctorRequest = new DoctorRequest();
//            doctorRequest.setName("Maria Valentina");
//            doctorRequest.setSurname("Barone");
//            doctorRequest.setLicenceNumber(faker.regexify("[A-Z0-9]{8}"));
//            doctorRequest.setPhoneNumber("3298180450");
//
//            AddressRequestForDoctor addressRequest = new AddressRequestForDoctor();
//            addressRequest.setStreet("Via Giusti");
//            addressRequest.setStreetNumber("5");
//            addressRequest.setProvinceAcronym("PA");
//            addressRequest.setCity("Palermo");
//            addressRequest.setPostalCode("90144");
//            addressRequest.setName("Studio Via Giusti");
//            doctorRequest.getAddresses().add(addressRequest);
//
//            request.setDoctor(doctorRequest);
//
//            try {
//                appUserSvc.registerDoctor(request);
//            } catch (RuntimeException e) {
//                System.out.println(e.getMessage());
//                System.out.println(request);
//            }
//        }
//
//
//    }
//}
