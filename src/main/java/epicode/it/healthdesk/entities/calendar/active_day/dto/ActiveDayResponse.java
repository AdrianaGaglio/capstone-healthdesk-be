package epicode.it.healthdesk.entities.calendar.active_day.dto;

import epicode.it.healthdesk.entities.calendar.time_slot.dto.TimeSlotResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ActiveDayResponse {
    private Long id;
    private String dayName;
    private Boolean isActive;
    private List<TimeSlotResponse> timeSlots = new ArrayList<>();
}
