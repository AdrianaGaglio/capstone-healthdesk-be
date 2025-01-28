package epicode.it.healthdesk.entities.calendar.time_slot.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotRequest {
    private LocalTime startTime;
    private LocalTime endTime;
}
