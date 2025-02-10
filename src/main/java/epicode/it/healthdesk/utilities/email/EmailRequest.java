package epicode.it.healthdesk.utilities.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailRequest {

    @Email(message = "Indirizzo destinatario non valido")
    private String to;

    @Email(message = "Indirizzo mittente non valido")
    private String from;

    @NotBlank(message = "Campo oggetto obbligatorio")
    private String subject;

    @NotBlank(message = "Corpo del messaggio obbligatorio")
    private String body;

    private String name;

    private String surname;

    private Long doctorId;

}
