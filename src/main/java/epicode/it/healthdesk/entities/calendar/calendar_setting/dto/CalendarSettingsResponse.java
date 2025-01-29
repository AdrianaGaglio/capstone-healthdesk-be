package epicode.it.healthdesk.entities.calendar.calendar_setting.dto;

import epicode.it.healthdesk.entities.calendar.active_day.dto.ActiveDayResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CalendarSettingsResponse {
    private List<ActiveDayResponse> activeDays = new ArrayList<>();
}
