package epicode.it.healthdesk.auth.appuser;

import epicode.it.healthdesk.auth.configurations.PwdEncoder;
import epicode.it.healthdesk.auth.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthRunner implements ApplicationRunner {
    private final AppUserSvc appUserSvc;

    private final PwdEncoder pwdEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<AppUser> admin = appUserSvc.findByEmail("admin");
        if (admin.isEmpty()) {
            RegisterRequest adminRequest = new RegisterRequest("admin@mail.com", "adminpwd");
            appUserSvc.registerAdmin(adminRequest);
        }


        Optional<AppUser> doctor = appUserSvc.findByEmail("doctor");
        if (doctor.isEmpty()) {
            RegisterRequest doctorRequest = new RegisterRequest("doctor@mail.com", "doctorpwd");
            appUserSvc.registerDoctor(doctorRequest);
        }

        Optional<AppUser> user = appUserSvc.findByEmail("user");
        if (user.isEmpty()) {
            RegisterRequest userRequest = new RegisterRequest("user@mail.com", "userpwd");
            appUserSvc.registerDoctor(userRequest);
        }

    }
}
