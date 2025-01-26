package epicode.it.healthdesk.auth.appuser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component // Indica che questa classe è un componente gestito da Spring.
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Metodo chiamato quando una richiesta non autorizzata tenta di accedere a una risorsa protetta.
     * @param request La richiesta HTTP ricevuta.
     * @param response La risposta HTTP da inviare.
     * @param authException L'eccezione di autenticazione che ha causato il fallimento.
     * @throws IOException Se si verifica un errore durante la scrittura della risposta.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Imposta il codice di stato della risposta HTTP a 401 (Non Autorizzato).
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Accesso non autorizzato");
    }
}
