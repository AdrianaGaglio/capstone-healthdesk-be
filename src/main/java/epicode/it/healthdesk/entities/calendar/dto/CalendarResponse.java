package epicode.it.healthdesk.entities.calendar.dto;

import epicode.it.healthdesk.entities.calendar.active_day.ActiveDay;
import epicode.it.healthdesk.entities.calendar.active_day.dto.ActiveDayResponse;
import epicode.it.healthdesk.entities.calendar.calendar_setting.CalendarSettings;
import epicode.it.healthdesk.entities.calendar.calendar_setting.dto.CalendarSettingsResponse;
import epicode.it.healthdesk.entities.doctor.dto.DoctorResponse;
import lombok.Data;

import java.util.List;

@Data
public class CalendarResponse {
    private Long id;
    private String doctorName;
    private CalendarSettingsResponse settings;

}
