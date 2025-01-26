package epicode.it.healthdesk.auth.appuser;

import epicode.it.healthdesk.auth.configurations.PwdEncoder;
import epicode.it.healthdesk.auth.dto.LoginRequest;
import epicode.it.healthdesk.auth.dto.RegisterRequest;
import epicode.it.healthdesk.auth.jwt.JwtTokenUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor // Genera automaticamente un costruttore con tutti i campi final, riducendo il boilerplate
@Validated
public class AppUserSvc {

    private final AppUserRepo appUserRepo; // Repository per gestire le operazioni di persistenza per AppUser
    private final PwdEncoder encoder; // Utilità per codificare le password
    private final AuthenticationManager authenticationManager; // Gestisce il processo di autenticazione
    private final JwtTokenUtil jwtTokenUtil; // Utilità per generare e gestire token JWT


    public String registerUser(@Valid RegisterRequest request) {

        if (appUserRepo.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("Email già in uso");
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());

        appUser.setPassword(encoder.passwordEncoder().encode(request.getPassword()));
        appUser.setRoles(Set.of(Role.ROLE_USER));

        appUserRepo.save(appUser);

        return "Utente registrato con successo";
    }

    public AppUser registerAdmin(@Valid RegisterRequest request) {

        if (appUserRepo.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("Email già in uso");
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());

        appUser.setPassword(encoder.passwordEncoder().encode(request.getPassword()));
        appUser.setRoles(Set.of(Role.ROLE_ADMIN));

        return appUserRepo.save(appUser);
    }

    public AppUser registerDoctor(@Valid RegisterRequest request) {

        if (appUserRepo.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("Email già in uso");
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());

        appUser.setPassword(encoder.passwordEncoder().encode(request.getPassword()));
        appUser.setRoles(Set.of(Role.ROLE_DOCTOR));

        return appUserRepo.save(appUser);
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
            throw new SecurityException("Credenziali non valide", e);
        }
    }


    public AppUser loadUserByEmail(String identifier) {
        return findByEmail(identifier).orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
    }
}
