package epicode.it.healthdesk.auth.dto;


import epicode.it.healthdesk.entities.patient.dto.PatientRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull(message = "Email richiesta")
    private String email;

    private String password;

    private String code;

    private PatientRequest patient;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
