package epicode.it.healthdesk.auth.configurations;


import epicode.it.healthdesk.auth.appuser.CustomUserDetailsService;
import epicode.it.healthdesk.auth.appuser.JwtAuthenticationEntryPoint;
import epicode.it.healthdesk.auth.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Indica che questa classe è una configurazione Spring.
@EnableWebSecurity // Abilita la sicurezza web di Spring Security.
@EnableGlobalMethodSecurity(prePostEnabled = true) // Abilita la sicurezza a livello di metodo tramite annotazioni come @PreAuthorize.
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    /**
     * Configura la catena di filtri di sicurezza.
     * @param http Configurazione di sicurezza HTTP.
     * @return La catena di filtri configurata.
     * @throws Exception In caso di errore durante la configurazione.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disabilita la protezione CSRF
                .csrf(csrf -> csrf.disable())
                // Configura le regole di autorizzazione per le richieste.
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Accesso libero alle API di Swagger.
                        //.requestMatchers("/api/**").permitAll() // Accesso libero a tutte le richieste API.
                        .anyRequest().permitAll() // Consente tutte le richieste per il momento (da restringere in produzione).
                )
                // Configura la gestione della sessione come stateless, poiché stiamo utilizzando JWT.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Aggiunge il filtro per la validazione dei token JWT prima del filtro di autenticazione standard.
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Costruisce e restituisce la catena di sicurezza configurata.
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
