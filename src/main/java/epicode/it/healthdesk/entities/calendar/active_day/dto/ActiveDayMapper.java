package epicode.it.healthdesk.entities.calendar.active_day.dto;

import epicode.it.healthdesk.entities.calendar.active_day.ActiveDay;
import epicode.it.healthdesk.entities.calendar.active_day.DayName;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActiveDayMapper {

    public ActiveDayResponse toActiveDayResponse(ActiveDay day) {
        ActiveDayResponse response = new ActiveDayResponse();
        BeanUtils.copyProperties(day, response);
        response.setDayName(day.getDayName().toString());
        return response;
    }

    public List<ActiveDayResponse> toActiveDayResponseList(List<ActiveDay> days) {
        return days.stream().map(this::toActiveDayResponse).toList();
    }
}
