package epicode.it.healthdesk.entities.calendar.active_day.dto;

import epicode.it.healthdesk.entities.calendar.active_day.ActiveDay;
import epicode.it.healthdesk.entities.calendar.active_day.DayName;
import epicode.it.healthdesk.entities.calendar.time_slot.dto.TimeSlotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActiveDayMapper {
    private final TimeSlotMapper slotMapper;

    public ActiveDayResponse toActiveDayResponse(ActiveDay day) {
        ActiveDayResponse response = new ActiveDayResponse();
        BeanUtils.copyProperties(day, response);
        response.setDayName(day.getDayName().toString());
        if (day.getSlots() != null && day.getSlots().size() > 0) {
            response.getTimeSlots().addAll(slotMapper.toTimeSlotResponseList(day.getSlots()));
        }
        return response;
    }

    public List<ActiveDayResponse> toActiveDayResponseList(List<ActiveDay> days) {
        return days.stream().map(this::toActiveDayResponse).toList();
    }
}
