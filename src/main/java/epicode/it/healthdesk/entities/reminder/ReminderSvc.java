package epicode.it.healthdesk.entities.reminder;

import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import epicode.it.healthdesk.entities.reminder.dto.ReminderRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class ReminderSvc {
    private final ReminderRepo reminderRepo;

    public List<Reminder> getAll() {
        return reminderRepo.findAll();
    }

    public Page<Reminder> getAllPageable(Pageable pageable) {
        return reminderRepo.findAll(pageable);
    }

    public Reminder getById(Long id) {
        return reminderRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Reminder not found"));
    }

    public int count() {
        return (int) reminderRepo.count();
    }

    public Reminder save(Reminder r) {
        return reminderRepo.save(r);
    }

    public String delete(Long id) {
        Reminder e = getById(id);
        reminderRepo.delete(e);
        return "Reminder cancellato con successo!";
    }

    public String delete(Reminder e) {
        Reminder foundReminder = getById(e.getId());
        reminderRepo.delete(foundReminder);
        return "Reminder cancellato con successo!";
    }

    public Reminder create(MedicalFolder mf, @Valid ReminderRequest request) {

        Reminder r = new Reminder();
        BeanUtils.copyProperties(request, r);
        r.setFrequency(Frequency.valueOf(request.getFrequency().toUpperCase()));
        r.setIsActive(true);
        r.setMedicalFolder(mf);
        return reminderRepo.save(r);
    }

    public List<Reminder> findByFrequency(Frequency frequency) {
        return reminderRepo.findByFrequency(frequency);
    }


}
