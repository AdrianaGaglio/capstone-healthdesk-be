package epicode.it.healthdesk.entities.calendar.active_day;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ActiveDayRepo extends JpaRepository<ActiveDay, Long> {


}
