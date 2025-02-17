package epicode.it.healthdesk.utilities.email;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service

@Validated
public class EmailSvc {

    @Autowired
    private FirebaseApp firebase;

    @Autowired
    private StorageClient storageClient;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailMapper mapper;

    @Autowired
    private DoctorSvc doctorSvc;

    public String sendEmail(@Valid EmailRequest request) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getBody());
        message.setFrom(request.getFrom() != null ? request.getFrom() : from);

        mailSender.send(message);
        return "Mail successfully sent to " + request.getTo();
    }

    public String sendEmailHtml(@Valid EmailRequest request) {

//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message);
//
//            helper.setTo(request.getTo());
//            helper.setSubject(request.getSubject());
//            helper.setText(request.getBody(), true);
//            helper.setFrom(request.getFrom() != null ? request.getFrom() : from);
//
//            mailSender.send(message);
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
        return "Mail inviata correttamente!";
    }

    public String sendContactMail(EmailRequest request) {
        Doctor d = doctorSvc.getById(request.getDoctorId());
        request.setBody(mapper.toContactMailBody(request, d));

        if (request.getFrom() == null) throw new IllegalArgumentException("Devi inserire un indirizzo email");
        request.setFrom(request.getFrom());

        request.setTo(d.getAppUser().getEmail()); // indirizzo del medico

        return sendEmailHtml(request);

    }

    public static String extractRelativePath(String url, String bucketName) {
        String prefix = "https://storage.googleapis.com/" + bucketName + "/";
        return url.replace(prefix, "");
    }

    public String sendEmailWithAttachment(@Valid EmailRequest request, String pathFile) {
        try {

            Storage storage = storageClient.bucket().getStorage();
            String bucketName = firebase.getOptions().getStorageBucket();

            // Ottieni il file da Firebase Storage
            Blob blob = storage.get(bucketName, extractRelativePath(pathFile, bucketName));

            if (blob == null) {
                throw new RuntimeException("File non trovato su Firebase Storage");
            }

            // Legge il file in un input stream
            InputStream fileStream = new ByteArrayInputStream(blob.getContent());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(request.getBody(), true);
            helper.setFrom(request.getFrom() != null ? request.getFrom() : from);

            // Allegare il file come DataSource
            ByteArrayDataSource dataSource = new ByteArrayDataSource(fileStream.readAllBytes(), "application/pdf");
            helper.addAttachment("documento.pdf", dataSource);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Mail inviata correttamente!";
    }


}
