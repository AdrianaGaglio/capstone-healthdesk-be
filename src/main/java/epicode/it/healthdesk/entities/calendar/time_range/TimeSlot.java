package epicode.it.healthdesk.entities.calendar.time_range;

import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

import java.time.LocalTime;

@Data
public class TimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeSlot(){}

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}


