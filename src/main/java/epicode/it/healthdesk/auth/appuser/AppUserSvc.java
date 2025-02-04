package epicode.it.healthdesk.auth.appuser;

import epicode.it.healthdesk.auth.dto.*;
import epicode.it.healthdesk.auth.jwt.JwtTokenUtil;

import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.general_user.GeneralUser;
import epicode.it.healthdesk.entities.general_user.GeneralUserRepo;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.patient.PatientSvc;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Validated
public class AppUserSvc {
    private final AppUserRepo appUserRepo;
    private final PasswordEncoder pwdEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PatientSvc patientSvc;
    private final DoctorSvc doctorSvc;
    private final GeneralUserRepo generalUserRepo;

    @Transactional
    public Patient registerPatient(@Valid RegisterRequest request) {

        if (appUserRepo.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("Email già in uso");
        }

        if (request.getPassword() == null) request.setPassword("temp_password");

        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setPassword(pwdEncoder.encode(request.getPassword()));

        appUser.setRoles(Set.of(Role.ROLE_PATIENT));
        appUser = appUserRepo.save(appUser);

        Patient p = patientSvc.create(request.getPatient());
        p.setAppUser(appUser);
        appUser.setGeneralUser(p);
        return p;
    }

    public AppUser registerAdmin(@Valid RegisterRequest request) {

        if (appUserRepo.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("Email già in uso");
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setPassword(pwdEncoder.encode(request.getPassword()));
        appUser.setRoles(Set.of(Role.ROLE_ADMIN));

        return appUserRepo.save(appUser);
    }


    @Transactional
    public String registerDoctor(@Valid RegisterDoctorRequest request) {
        if (appUserRepo.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("Email già in uso");
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());

        appUser.setPassword(pwdEncoder.encode(request.getPassword()));

        appUser.setRoles(Set.of(Role.ROLE_DOCTOR));

        appUser = appUserRepo.save(appUser);

        Doctor d = doctorSvc.create(request.getDoctor());
        d.setAppUser(appUser);
        appUser.setGeneralUser(d);

        return "Medico registrato con successo";
    }

    public Optional<AppUser> findByEmail(String email) {

        return appUserRepo.findByEmail(email);
    }


    public String authenticateUser(@Valid LoginRequest request) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            System.out.println("dentro metodo di autenticazione");
            throw new SecurityException("Credenziali non valide", e);
        }
    }


    public AppUser loadUserByEmail(String identifier) {
        return findByEmail(identifier).orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
    }

    @Transactional
    public AppUser updateLoginInfo(AppUser appUser, @Valid AuthUpdateRequest request) {

        if (request.getEmail() != null) {
            appUser.setEmail(request.getEmail());
        }
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty() && request.getOldPassword() != null && !request.getOldPassword().isEmpty()) {
            if (pwdEncoder.matches(request.getOldPassword(), appUser.getPassword())) {
                appUser.setPassword(pwdEncoder.encode(request.getNewPassword()));
            } else {
                throw new SecurityException("Credenziali non valide");
            }
        }
        return appUserRepo.save(appUser);
    }

}
