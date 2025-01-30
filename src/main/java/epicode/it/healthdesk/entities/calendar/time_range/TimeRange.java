package epicode.it.healthdesk.entities.calendar.time_range;

import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDay;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
@Table(name = "time_ranges")
public class TimeRange {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalTime startTime;
    private LocalTime endTime;

    @OneToOne
    @JoinColumn(name = "opening_day_id")
    private OpeningDay openingDay;
}