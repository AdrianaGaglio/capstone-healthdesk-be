package epicode.it.healthdesk.entities.calendar.dto;

import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.calendar.active_day.dto.ActiveDayMapper;
import epicode.it.healthdesk.entities.calendar.time_slot.dto.TimeSlotMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalendarMapper {
    private final ActiveDayMapper dayMapper;

    private ModelMapper mapper = new ModelMapper();

    public CalendarResponse toCalendarResponse(Calendar c) {
        CalendarResponse response = mapper.map(c, CalendarResponse.class);
        response.setDoctorName(c.getDoctor().getName() + " " + c.getDoctor().getSurname());
        response.setActiveDays(dayMapper.toActiveDayResponseList(c.getActiveDays()));
        return response;
    }
}
