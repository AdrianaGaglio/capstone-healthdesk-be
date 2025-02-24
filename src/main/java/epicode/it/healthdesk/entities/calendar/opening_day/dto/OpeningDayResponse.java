package epicode.it.healthdesk.entities.calendar.opening_day.dto;

import epicode.it.healthdesk.entities.calendar.time_range.TimeRange;
import epicode.it.healthdesk.entities.calendar.time_range.TimeSlot;
import epicode.it.healthdesk.entities.calendar.time_range.dto.TimeRangeResponse;
import lombok.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OpeningDayResponse {
    private Long id;
    private String dayName;
    private List<TimeSlot> slots;
    private List<TimeSlot> extraRange;
    private Boolean isActive;
    private Boolean hasExtraRange;
}
