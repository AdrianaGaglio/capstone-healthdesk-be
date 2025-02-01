package epicode.it.healthdesk.entities.appointment;

import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.service.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    @Query("SELECT a From Appointment a WHERE a.calendar.id = :calendarId AND a.startDate = :startDate")
    public Optional<Appointment> findFirstByCalendarIdAndStartDate(@Param("calendarId") Long calendarId, @Param("startDate") LocalDateTime startDate);

    public List<Appointment> findByService(DoctorService service);

    Page<Appointment> findByCalendarAndStartDateAfter(Calendar calendar, LocalDateTime date, Pageable pageable);
}
