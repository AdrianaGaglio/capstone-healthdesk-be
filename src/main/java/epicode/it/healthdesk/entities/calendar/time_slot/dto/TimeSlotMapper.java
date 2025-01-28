package epicode.it.healthdesk.entities.calendar.time_slot.dto;

import epicode.it.healthdesk.entities.calendar.time_slot.TimeSlot;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimeSlotMapper {

    public TimeSlotResponse toTimeSlotResponse(TimeSlot slot) {
        TimeSlotResponse response = new TimeSlotResponse();
        BeanUtils.copyProperties(slot, response);
        return response;
    }

    public List<TimeSlotResponse> toTimeSlotResponseList(List<TimeSlot> slots) {
        return slots.stream().map(this::toTimeSlotResponse).toList();
    }
}
