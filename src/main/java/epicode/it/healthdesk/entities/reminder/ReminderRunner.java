package epicode.it.healthdesk.entities.reminder;

import com.github.javafaker.Faker;
import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import epicode.it.healthdesk.entities.medial_folder.MedicalFolderSvc;
import epicode.it.healthdesk.entities.reminder.dto.ReminderRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Order(11)
public class ReminderRunner implements ApplicationRunner {
    private final ReminderSvc reminderSvc;
    private final MedicalFolderSvc mfSvc;
    private final Faker faker;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<MedicalFolder> mfs = mfSvc.getAll();

        if(reminderSvc.count() == 0) {

            mfs.forEach(mf -> {
                ReminderRequest r = new ReminderRequest();
                r.setDescription(faker.lorem().fixedString(30));
                r.setStartDate(faker.date().future(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                r.setFrequency(faker.random().nextInt(1,2) == 1 ? Frequency.WEEKLY.toString() : Frequency.MONTHLY.toString());
                r.setMedicalFolderId(mf.getId());
                mfSvc.addReminder(mf.getId(), r);
            });

        }
    }
}
