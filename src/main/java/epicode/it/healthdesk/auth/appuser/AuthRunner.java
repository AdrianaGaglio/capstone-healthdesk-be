package epicode.it.healthdesk.auth.appuser;

import epicode.it.healthdesk.auth.dto.RegisterRequest;
import epicode.it.healthdesk.utilities.email.EmailRequest;
import epicode.it.healthdesk.utilities.email.EmailSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Order(1)
public class AuthRunner implements ApplicationRunner {
    private final AppUserSvc appUserSvc;
    private final PasswordEncoder pwdEncoder;
    private final EmailSvc emailSvc;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        String code = appUserSvc.generateAuthCode("adriana.gaglio@gmail.com");
//        EmailRequest mail = new EmailRequest();
//        mail.setTo("adriana.gaglio@gmail.com");
//        mail.setSubject("Health Desk - Registrazione amministratore");
//        mail.setBody("Codice autorizzazione: " + code);
//        emailSvc.sendEmail(mail);

        Optional<AppUser> admin = appUserSvc.findByEmail("admin@mail.com");
        if (admin.isEmpty()) {
            RegisterRequest adminRequest = new RegisterRequest("admin@mail.com", "adminpwd");
            appUserSvc.registerAdmin(adminRequest);
        }


//        Optional<AppUser> doctor = appUserSvc.findByEmail("doctor@mail.com");
//        if (doctor.isEmpty()) {
//            RegisterRequest doctorRequest = new RegisterRequest("doctor@mail.com", "doctorpwd");
//            appUserSvc.registerDoctor(doctorRequest);
//        }
//
//        Optional<AppUser> patient = appUserSvc.findByEmail("patient@mail.com");
//        if (patient.isEmpty()) {
//            RegisterRequest patientRequest = new RegisterRequest("patient@mail.com", "patientpwd");
//            appUserSvc.registerPatient(patientRequest);
//        }

    }
}
