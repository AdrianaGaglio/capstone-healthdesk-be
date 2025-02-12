package epicode.it.healthdesk.entities.reminder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepo extends JpaRepository<Reminder, Long> {

    public List<Reminder> findByFrequency(Frequency f);

}
