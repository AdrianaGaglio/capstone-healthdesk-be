package epicode.it.healthdesk.entities.calendar.dto;

import com.github.javafaker.Bool;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HolidayRequest {
    Long calendarId;
    Boolean onHoliday;
    LocalDate holidayDateStart;
    LocalDate holidayDateEnd;
}
