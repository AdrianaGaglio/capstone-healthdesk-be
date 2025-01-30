package epicode.it.healthdesk.entities.calendar.dto;

import epicode.it.healthdesk.entities.appointment.dto.AppointmentMapper;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentResponse;
import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.calendar.opening_day.dto.OpeningDayMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalendarMapper {
    private final AppointmentMapper appointmentMapper;
    private final OpeningDayMapper dayMapper;

    private ModelMapper mapper = new ModelMapper();


    public CalendarResponse toCalendarResponse(Calendar c) {
        CalendarResponse response = mapper.map(c, CalendarResponse.class);
        response.setAppointments(appointmentMapper.toAppointmentResponseForCalendarList(c.getAppointments()));
        response.setDays(dayMapper.toOpeningDayResponseList(c.getDays()));
        return response;
    }
}
