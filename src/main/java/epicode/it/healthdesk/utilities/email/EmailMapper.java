package epicode.it.healthdesk.utilities.email;

import epicode.it.healthdesk.auth.appuser.AppUser;
import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailMapper {

    private String website = "http://localhost:4200";

    @Value("${spring.mail.username}")
    private String from;


    public String toContactMailBody(EmailRequest request, Doctor d) {
        String template = loadTemplate("src/main/resources/templates/contact-mail.html");
        Map<String, String> values = new HashMap<>();
        values.put("subject", request.getSubject());
        values.put("doctorName", d.getName() + " " + d.getSurname());
        values.put("body", request.getBody());
        values.put("website", website);
        values.put("user_name", request.getName() != null ? request.getName() : "");
        values.put("user_surname", request.getSurname() != null ? request.getSurname() : "");
        return processTemplate(template, values);
    }

    public String toNewUserBody(EmailRequest request, String token) {
        String template = loadTemplate("src/main/resources/templates/new-user.html");
        Map<String, String> values = new HashMap<>();
        values.put("user_name", request.getName());
        values.put("user_surname", request.getSurname());
        values.put("email", request.getTo());
        values.put("website", website);
        values.put("reset", website + "/auth/reset-password/true/" + token);
        return processTemplate(template, values);
    }

    @Transactional
    public String toAppConfirmation(Appointment app) {
        String template = loadTemplate("src/main/resources/templates/app-confirmation.html");
        Map<String, String> values = new HashMap<>();
        values.put("user_name", app.getMedicalFolder().getPatient().getName());
        values.put("user_surname", app.getMedicalFolder().getPatient().getSurname());
        values.put("startDate", app.getStartDate().toString());
        values.put("doctorService", app.getService().getName());
        values.put("doctorName", app.getCalendar().getDoctor().getName() + " " + app.getCalendar().getDoctor().getSurname());
        values.put("confirm", "http://localhost:4200/");
        return processTemplate(template, values);
    }

    private String loadTemplate(String filePath)  {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String processTemplate(String template, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;
    }

}
