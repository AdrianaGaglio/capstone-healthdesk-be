package epicode.it.healthdesk.utilities.email;

import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailSvc emailSvc;
    private final EmailMapper mapper;
    private final DoctorSvc doctorSvc;


    @PostMapping("/{isHtml}")
    public ResponseEntity<String> sendHtmlEmail(@RequestBody EmailRequest request, @RequestParam boolean isHtml) {
        if (isHtml) {
            return new ResponseEntity<>(emailSvc.sendEmailHtml(request), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(emailSvc.sendEmail(request), HttpStatus.CREATED);
        }
    }

    @PostMapping("/contact")
    public ResponseEntity<Map<String, String>> sendContactMail(@RequestBody EmailRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", emailSvc.sendContactMail(request));
        return ResponseEntity.ok(response);
    }
}
