package epicode.it.healthdesk.auth.appuser;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="auth_code")
@Data
public class AuthCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String code;

    private String email;

    private Boolean valid;
}
