package epicode.it.healthdesk.entities.calendar;

import epicode.it.healthdesk.entities.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendarRepo extends JpaRepository<Calendar, Long> {

    public Optional<Calendar> findByDoctor(Doctor d);
}
