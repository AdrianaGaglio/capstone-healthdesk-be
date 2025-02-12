package epicode.it.healthdesk.entities.reminder.dto;

import epicode.it.healthdesk.entities.reminder.Reminder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReminderMapper {
    ModelMapper mapper = new ModelMapper();

    public ReminderResponse toReminderResponse(Reminder r) {
        ReminderResponse response = mapper.map(r, ReminderResponse.class);
        response.setFrequency(r.getFrequency().toString());
        return response;
    }

    public List<ReminderResponse> toReminderResponseList(List<Reminder> reminders) {
        return reminders.stream().map(this::toReminderResponse).toList();
    }
}
