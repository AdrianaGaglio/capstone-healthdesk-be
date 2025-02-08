package epicode.it.healthdesk.entities.calendar.opening_day.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class OpeningDayNewRange {
    private LocalTime startTime;
    private LocalTime endTime;
}
