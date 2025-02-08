package epicode.it.healthdesk.entities.calendar.time_range;

import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRangeRepo extends JpaRepository<TimeRange, Long> {

    public boolean existsByOpeningDay(OpeningDay openingDay);

    public TimeRange findByOpeningDay(OpeningDay openingDay);
}
