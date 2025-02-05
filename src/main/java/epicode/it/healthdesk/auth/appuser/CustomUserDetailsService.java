package epicode.it.healthdesk.auth.appuser;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserRepo appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Cerca un utente nel database in base all'email
        AppUser appUser = appUserRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));

        // Converte l'utente in UserDetails
        return new User(
                appUser.getEmail(), // Imposta l'email dell'utente come username
                appUser.getPassword(), // Imposta la password dell'utente (già codificata).
                appUser.getRoles().stream() // Converte i ruoli dell'utente in SimpleGrantedAuthority.
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toList()) // Raccoglie i ruoli in una lista.
        );
    }
}
