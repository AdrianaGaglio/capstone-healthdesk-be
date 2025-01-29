package epicode.it.healthdesk.entities.calendar.time_slot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Optional;

public interface TimeSlotRepo extends JpaRepository<TimeSlot, Long> {

    @Query("SELECT s From TimeSlot s WHERE s.startTime = :startTime AND s.activeDay.id = :dayId")
    public Optional<TimeSlot> findByStartTimeAndActiveDayId(@Param("startTime") LocalTime startTime, @Param("dayId") Long dayId);
}
