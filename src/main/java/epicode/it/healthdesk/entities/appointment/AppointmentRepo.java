package epicode.it.healthdesk.entities.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    @Query("SELECT a From Appointment a WHERE a.calendar.id = :calendarId AND a.startDate = :startDate")
    public Optional<Appointment> findFirstByCalendarIdAndStartDate(@Param("calendarId") Long calendarId, @Param("startDate") LocalDateTime startDate);
}
