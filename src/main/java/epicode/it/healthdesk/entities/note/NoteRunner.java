package epicode.it.healthdesk.entities.note;

import com.github.javafaker.Faker;
import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.appointment.AppointmentStatus;
import epicode.it.healthdesk.entities.appointment.AppointmentSvc;
import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import epicode.it.healthdesk.entities.medial_folder.MedicalFolderSvc;
import epicode.it.healthdesk.entities.note.dto.NoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Order(11)
@RequiredArgsConstructor
public class NoteRunner implements ApplicationRunner {
    private final Faker faker;
    private final NoteSvc noteSvc;
    private final MedicalFolderSvc medicalFolderSvc;



    @Override
    public void run(ApplicationArguments args) throws Exception {


        if(noteSvc.count() == 0) {

            List<MedicalFolder> mfolders = medicalFolderSvc.getAll();

            mfolders.forEach(mf -> {
                List<Appointment> apps = mf.getAppointments().stream().filter(a -> a.getStartDate().isBefore(LocalDateTime.now()) && a.getStatus() != AppointmentStatus.BLOCKED).toList();
                apps.forEach(a -> {
                    NoteRequest nr = new NoteRequest();
                    nr.setTitle(faker.lorem().sentence());
                    nr.setDescription(faker.lorem().sentence());
                    nr.setDate(a.getStartDate().toLocalDate());
                    medicalFolderSvc.addNote(mf.getId(), nr);
                });

            });

        }
    }
}
