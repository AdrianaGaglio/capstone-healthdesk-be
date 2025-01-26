package epicode.it.healthdesk.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull(message = "Identifier is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;
}
