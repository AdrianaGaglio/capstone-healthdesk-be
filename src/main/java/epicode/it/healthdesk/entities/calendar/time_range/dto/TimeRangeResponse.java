package epicode.it.healthdesk.entities.calendar.time_range.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeRangeResponse {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
}
