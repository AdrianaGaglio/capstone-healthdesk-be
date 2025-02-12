package epicode.it.healthdesk.utilities.email;

import epicode.it.healthdesk.auth.appuser.AppUser;
import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.patient.Patient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
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
        String day = app.getStartDate().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("it"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = app.getStartDate().format(formatter);
        values.put("day", day);
        values.put("startDate", formattedDate);
        values.put("doctorService", app.getService().getName());
        values.put("doctorName", app.getCalendar().getDoctor().getName() + " " + app.getCalendar().getDoctor().getSurname());
        values.put("confirm", website + "/dettagli-appuntamento/" + app.getId() + "/true");
        return processTemplate(template, values);
    }

    @Transactional
    public String toAppConfirmationForDoctor(Appointment app) {
        String template = loadTemplate("src/main/resources/templates/app-confirmation.html");
        Map<String, String> values = new HashMap<>();
        String day = app.getStartDate().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("it"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = app.getStartDate().format(formatter);
        values.put("day", day);
        values.put("startDate", formattedDate);
        values.put("doctorService", app.getService().getName());
        values.put("doctorName", app.getCalendar().getDoctor().getName() + " " + app.getCalendar().getDoctor().getSurname());
        values.put("confirm", website + "/dettagli-appuntamento/" + app.getId() + "/true");
        return processTemplate(template, values);
    }

    public String toNewAppointment(Appointment app, boolean forDoctor) {
        String template = "";
        if(forDoctor) {
            template = loadTemplate("src/main/resources/templates/new-appointment-for-doctor.html");
        } else {
            template = loadTemplate("src/main/resources/templates/new-appointment.html");
        }

        Map<String, String> values = new HashMap<>();
        String day = app.getStartDate().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("it"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = app.getStartDate().format(formatter);
        values.put("user_name", app.getMedicalFolder().getPatient().getName());
        values.put("user_surname", app.getMedicalFolder().getPatient().getSurname());
        values.put("day", day);
        values.put("startDate", formattedDate);
        values.put("doctorService", app.getService().getName());
        values.put("doctorName", app.getCalendar().getDoctor().getName() + " " + app.getCalendar().getDoctor().getSurname());
        String location = "";
        if(app.getOnline()) {
            location = "Online";
        } else {
            location = app.getDoctorAddress().getStreet() + ", " + app.getDoctorAddress().getStreetNumber() + ", " +
                    app.getDoctorAddress().getCity();
        }
        values.put("location",location);
        values.put("confirm", website + "/dettagli-appuntamento/" + app.getId() + "/true");
        return processTemplate(template, values);
    }

    public String toAppointmentStatusChange(Appointment app, String status) {
        String template = loadTemplate("src/main/resources/templates/appointment-status-change.html");
        Map<String, String> values = new HashMap<>();
        String day = app.getStartDate().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("it"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = app.getStartDate().format(formatter);
        values.put("day", day);
        values.put("startDate", formattedDate);
        values.put("doctorService", app.getService().getName());
        values.put("user_name", app.getMedicalFolder().getPatient().getName());
        values.put("user_surname", app.getMedicalFolder().getPatient().getSurname());
        String location = "";
        if(app.getOnline()) {
            location = "Online";
        } else {
            location = app.getDoctorAddress().getStreet() + ", " + app.getDoctorAddress().getStreetNumber() + ", " +
                       app.getDoctorAddress().getCity();
        }
        values.put("location", location);
        values.put("confirm", website +"/dettagli-appuntamento/" + app.getId() + "/true");
        values.put("status", status);
        return processTemplate(template, values);
    }

    public String toAppointmentDateChange(Appointment app, boolean forDoctor) {
        String template = "";
        if(forDoctor) {
            template = loadTemplate("src/main/resources/templates/appointment-date-change-for-doctor.html");
        } else {
            template = loadTemplate("src/main/resources/templates/appointment-date-change-for-patient.html");
        }
        Map<String, String> values = new HashMap<>();
        String day = app.getStartDate().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("it"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = app.getStartDate().format(formatter);
        values.put("day", day);
        values.put("startDate", formattedDate);
        String location = "";
        if(app.getOnline()) {
            location = "Online";
        } else {
            location = app.getDoctorAddress().getStreet() + ", " + app.getDoctorAddress().getStreetNumber() + ", " +
                       app.getDoctorAddress().getCity();
        }
        values.put("location", location);
        values.put("user_name", app.getMedicalFolder().getPatient().getName());
        values.put("user_surname", app.getMedicalFolder().getPatient().getSurname());
        values.put("confirm", website + "/dettagli-appuntamento/" + app.getId() + "/true");
        values.put("doctorService", app.getService().getName());
        return processTemplate(template, values);
    }

    public String toNewPrescription(Patient p) {
        String template = loadTemplate("src/main/resources/templates/new-prescription.html");
        Map<String, String> values = new HashMap<>();
        values.put("user_name", p.getName());
        values.put("user_surname", p.getSurname());
        values.put("confirm", website + "/paziente/scheda-personale");
        return processTemplate(template, values);
    }

    public String toNewDocument(Patient p) {
        String template = loadTemplate("src/main/resources/templates/new-document.html");
        Map<String, String> values = new HashMap<>();
        values.put("user_name", p.getName());
        values.put("user_surname", p.getSurname());
        values.put("confirm", website + "/paziente/scheda-personale");
        return processTemplate(template, values);
    }

    private String loadTemplate(String filePath)  {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toAppCancellationForUser(Appointment app) {
        String template = loadTemplate("src/main/resources/templates/appointment-cancellation-for-user.html");
        Map<String, String> values = new HashMap<>();
        String day = app.getStartDate().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("it"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = app.getStartDate().format(formatter);
        values.put("day", day);
        values.put("startDate", formattedDate);
        values.put("doctorService", app.getService().getName());
        values.put("user_name", app.getMedicalFolder().getPatient().getName());
        values.put("user_surname", app.getMedicalFolder().getPatient().getSurname());
        values.put("doctorName", app.getCalendar().getDoctor().getName() + " " + app.getCalendar().getDoctor().getSurname());
        String location = "";
        if(app.getOnline()) {
            location = "Online";
        } else {
            location = app.getDoctorAddress().getStreet() + ", " + app.getDoctorAddress().getStreetNumber() + ", " +
                       app.getDoctorAddress().getCity();
        }
        values.put("location", location);
        values.put("confirm", "http://localhost:4200/prenota");
        return processTemplate(template, values);
    }

    public String toResetPassword(AppUser user, String token, boolean firstAccess) {
        String template = loadTemplate("src/main/resources/templates/reset-password.html");
        Map<String, String> values = new HashMap<>();
        String firstAccessString = firstAccess ? "true/" : "false/";
        values.put("reset", "http://localhost:4200/auth/reset-password/" + firstAccessString + token);
        values.put("user_name", user.getGeneralUser().getName());
        values.put("user_surname", user.getGeneralUser().getSurname());
        return processTemplate(template, values);
    }

    public String toReminder(Patient p) {
        String template = loadTemplate("src/main/resources/templates/reminder.html");
        Map<String, String> values = new HashMap<>();
        values.put("confirm", website + "/paziente/scheda-personale");
        values.put("user_name", p.getName());
        values.put("user_surname", p.getSurname());
        return processTemplate(template, values);
    }

    private String processTemplate(String template, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;
    }

}
