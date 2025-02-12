package epicode.it.healthdesk.entities.reminder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepo extends JpaRepository<Reminder, Long> {
}
