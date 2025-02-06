package epicode.it.healthdesk.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class Message {
    private String message;
    private HttpStatus status;
}