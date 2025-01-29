package epicode.it.healthdesk.entities.calendar.dto;

import epicode.it.healthdesk.entities.appointment.dto.AppointmentMapper;
import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.calendar.active_day.dto.ActiveDayMapper;
import epicode.it.healthdesk.entities.calendar.calendar_setting.CalendarSettings;
import epicode.it.healthdesk.entities.calendar.calendar_setting.dto.CalendarSettingsResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalendarMapper {
    private final ActiveDayMapper dayMapper;
    private final AppointmentMapper appointmentMapper;


    private ModelMapper mapper = new ModelMapper();

    public CalendarResponse toCalendarResponse(Calendar c) {
        CalendarResponse response = mapper.map(c, CalendarResponse.class);
        response.setDoctorName(c.getDoctor().getName() + " " + c.getDoctor().getSurname());
        response.setSettings(toCalendarSettingsResponse(c.getSettings()));
        response.setAppointments(appointmentMapper.toAppointmentResponseForCalendarList(c.getAppointments()));
        return response;
    }

    public CalendarSettingsResponse toCalendarSettingsResponse(CalendarSettings settings) {
        CalendarSettingsResponse response = new CalendarSettingsResponse();
        response.setActiveDays(dayMapper.toActiveDayResponseList(settings.getActiveDays()));
        return response;
    }
}
