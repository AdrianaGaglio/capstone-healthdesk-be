//package epicode.it.healthdesk.entities.patient;
//
//import com.github.javafaker.Faker;
//import epicode.it.healthdesk.auth.appuser.AppUserSvc;
//import epicode.it.healthdesk.auth.dto.RegisterRequest;
//import epicode.it.healthdesk.entities.address.city.CitySvc;
//import epicode.it.healthdesk.entities.address.city.dto.CityDTO;
//import epicode.it.healthdesk.entities.address.dto.AddressRequest;
//import epicode.it.healthdesk.entities.address.province.ProvinceSvc;
//import epicode.it.healthdesk.entities.address.province.dto.ProvinceDTO;
//import epicode.it.healthdesk.entities.doctor.Doctor;
//import epicode.it.healthdesk.entities.doctor.DoctorSvc;
//import epicode.it.healthdesk.entities.patient.dto.PatientRequest;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.time.ZoneId;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@Order(10)
//@RequiredArgsConstructor
//public class AddPatientsToDoctorRunner implements ApplicationRunner {
//    private final DoctorSvc doctorSvc;
//    private final PatientSvc patientSvc;
//
//
//    @Transactional
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//
//        Doctor d = doctorSvc.getByEmail("infohealthdesk@gmail.com");
//
//        if(d.getPatients().size() == 0) {
//            List<Patient> patients = patientSvc.getAll();
//            for (Patient p : patients) {
//                d.getPatients().add(p);
//                doctorSvc.save(d);
//            }
//        }
//
//
//
//    }
//}
