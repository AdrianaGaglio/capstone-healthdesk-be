package epicode.it.healthdesk.auth.appuser;

import epicode.it.healthdesk.auth.dto.*;
import epicode.it.healthdesk.auth.jwt.JwtTokenUtil;

import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.general_user.GeneralUser;
import epicode.it.healthdesk.entities.general_user.GeneralUserRepo;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.patient.PatientSvc;
import epicode.it.healthdesk.utilities.email.EmailMapper;
import epicode.it.healthdesk.utilities.email.EmailRequest;
import epicode.it.healthdesk.utilities.email.EmailSvc;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
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
    private final EmailSvc emailSvc;
    private final EmailMapper emailMapper;

    @Transactional
    public Patient registerPatient(@Valid RegisterRequest request) {
        // controllo se l'email esiste
        if (appUserRepo.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("Email già in uso");
        }

        // se non esiste, ne viene generata una temporanea
        // (per l'inserimento dei dati del paziente in fase di prenotazione)
        if (request.getPassword() == null) request.setPassword("temp_password");

        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setPassword(pwdEncoder.encode(request.getPassword()));

        appUser.setRoles(Set.of(Role.ROLE_PATIENT));
        appUser = appUserRepo.save(appUser);

        Patient p = patientSvc.create(request.getPatient());
        p.setAppUser(appUser);
        appUser.setGeneralUser(p);

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(request.getEmail());
        emailRequest.setName(p.getName());
        emailRequest.setSurname(p.getSurname());
        emailRequest.setSubject("HealthDesk - " + p.getName() + " " + p.getSurname() + " - Nuovo account creato");

        String token = jwtTokenUtil.generateAccessToken(appUser);
        emailRequest.setBody(emailMapper.toNewUserBody(emailRequest, token));
        emailSvc.sendEmail(emailRequest);
        return p;
    }

    // metodo per la registrazione di un nuovo amministratore
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


    // metodo per la registrazione di un nuovo medico
    @Transactional
    public String registerDoctor(@Valid RegisterDoctorRequest request) {

        // controllo se l'email è già registrata
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

    // cerca utente per email
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepo.findByEmail(email);
    }

    // metodo per il login
    @Transactional
    public String authenticateUser(@Valid LoginRequest request) {
        try {
            AppUser u = loadUserByEmail(request.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            u.getGeneralUser().setLastSeenOnline(LocalDate.now());
            appUserRepo.save(u);
            return jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            throw new SecurityException("Credenziali non valide", e);
        }
    }

    // carica utente per email
    public AppUser loadUserByEmail(String identifier) {
        return findByEmail(identifier).orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
    }

    // metodo per modificare dati di accesso utente
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

    @Transactional
    public AuthResponse resetPassword(@Valid ResetPassword request) {
        String email = jwtTokenUtil.getUsernameFromToken(request.getToken());
        AppUser u = findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        if(request.getOldPassword()== null) {
            request.setOldPassword("temp_password");
        }

        if(pwdEncoder.matches(request.getOldPassword(), u.getPassword())) {
            u.setPassword(pwdEncoder.encode(request.getPassword()));
        }

        u.getGeneralUser().setLastSeenOnline(LocalDate.now());
        appUserRepo.save(u);

        String token = jwtTokenUtil.generateAccessToken(u);
        String role = jwtTokenUtil.getRolesFromToken(token);

        return new AuthResponse(token, role);

    }
}
