package epicode.it.healthdesk.auth.appuser;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Indica che questa classe è una configurazione Spring.
public class OpenApiConfig {

    /**
     * Configura OpenAPI con uno schema di sicurezza per supportare l'autenticazione JWT.
     * @return Un'istanza personalizzata di OpenAPI.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth"; // Nome dello schema di sicurezza.

        // Crea e restituisce una configurazione OpenAPI personalizzata.
        return new OpenAPI()
                // Aggiunge un requisito di sicurezza che utilizza lo schema definito.
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // Configura i componenti con lo schema di sicurezza.
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP) // Specifica che si tratta di un'autenticazione HTTP.
                                .scheme("bearer") // Specifica che lo schema è di tipo Bearer Token.
                                .bearerFormat("JWT") // Indica che il formato del token è JWT.
                                .description("Inserisci il token JWT nel formato: Bearer {token}") // Descrizione per gli utenti.
                ));
    }
}