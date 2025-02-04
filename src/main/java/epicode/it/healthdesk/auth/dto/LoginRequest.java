package epicode.it.healthdesk.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull(message = "Identifier is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;
}
