package epicode.it.healthdesk.entities.calendar.opening_day.dto;

import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDay;
import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDayRepo;
import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDaySvc;
import epicode.it.healthdesk.entities.calendar.time_range.dto.TimeRangeResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpeningDayMapper {
    private final OpeningDaySvc daySvc;

    private ModelMapper mapper = new ModelMapper();

    public OpeningDayResponse toOpeningDayResponse(OpeningDay day) {
        OpeningDay dayDb = daySvc.getByNameAndCalendarId(day.getCalendar().getId(), day.getDayName());
        OpeningDayResponse response = mapper.map(day, OpeningDayResponse.class);
        response.setSlots(daySvc.getSlots(day));
        return response;
    }

    public List<OpeningDayResponse> toOpeningDayResponseList(List<OpeningDay> days) {
        return days.stream().map(this::toOpeningDayResponse).toList();
    }
}
