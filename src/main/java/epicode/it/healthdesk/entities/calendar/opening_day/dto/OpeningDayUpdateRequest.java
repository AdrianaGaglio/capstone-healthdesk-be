package epicode.it.healthdesk.entities.calendar.opening_day.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class OpeningDayUpdateRequest {
    @NotNull(message="Giorno richiesto")
    private String dayName;
    private Boolean isActive;
    private LocalTime startTime;
    private LocalTime endTime;
}
