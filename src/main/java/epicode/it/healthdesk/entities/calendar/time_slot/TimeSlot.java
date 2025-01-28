package epicode.it.healthdesk.entities.calendar.time_slot;

import epicode.it.healthdesk.entities.calendar.active_day.ActiveDay;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
@Table(name = "time_slots")
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne
    private ActiveDay activeDay;

}