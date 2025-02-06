package epicode.it.healthdesk.auth.jwt;

import epicode.it.healthdesk.auth.appuser.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Indica che questa classe è un componente Spring.
@RequiredArgsConstructor // Genera un costruttore con tutti i campi final automaticamente.
public class JwtRequestFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService; // Gestisce il caricamento degli utenti.
    private final JwtTokenUtil jwtTokenUtil; // Gestisce la validazione e l'estrazione di dati dai token JWT.

    /**
     * Metodo principale del filtro, eseguito per ogni richiesta.
     * @param request La richiesta HTTP ricevuta.
     * @param response La risposta HTTP da inviare.
     * @param chain La catena di filtri da eseguire.
     * @throws ServletException In caso di errore durante il filtraggio.
     * @throws IOException In caso di errore di input/output.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        boolean tokenExpired = false;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Impossibile ottenere il token JWT");
            } catch (ExpiredJwtException e) {
                System.out.println("Il token JWT è scaduto, generando un nuovo token...");
                username = e.getClaims().getSubject(); // Recupera lo username dal token scaduto
                tokenExpired = true;
            }
        } else {
            chain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

            // Se il token è scaduto, genero uno nuovo e lo uso per la validazione
            if (tokenExpired) {
                jwtToken = jwtTokenUtil.generateToken(userDetails); // Sostituiamo il vecchio token con quello nuovo
                response.setHeader("Authorization", "Bearer " + jwtToken);
            }

            // Ora validiamo il token CORRETTO (nuovo se rigenerato, vecchio se ancora valido)
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        chain.doFilter(request, response);
    }

}