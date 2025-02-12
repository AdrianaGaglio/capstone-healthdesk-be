package epicode.it.healthdesk.entities.reminder.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReminderResponse {
    private Long id;
    private String description;
    private String frequency;
    private LocalDate startDate;
    private Boolean isActive;

}
