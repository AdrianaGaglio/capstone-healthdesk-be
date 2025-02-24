package epicode.it.healthdesk.entities.calendar.opening_day;

import epicode.it.healthdesk.entities.calendar.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;

public interface OpeningDayRepo extends JpaRepository<OpeningDay, Long> {

    @Query("SELECT d From OpeningDay d WHERE d.dayName = :dayName AND d.calendar.id = :calendarId")
    public OpeningDay findFirstByNameAndCalendarId(@Param("calendarId") Long calendarId, @Param("dayName") DayOfWeek dayName);

    public List<OpeningDay> findAllByCalendar(Calendar calendar);

}
