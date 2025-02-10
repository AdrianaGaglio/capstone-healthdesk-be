package epicode.it.healthdesk.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPassword {
    @NotNull(message="Nuova password richiesta")
    private String password;

    private String oldPassword;

    private String token;


}
