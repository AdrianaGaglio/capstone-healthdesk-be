package epicode.it.healthdesk.entities.calendar.dto;

import epicode.it.healthdesk.entities.appointment.dto.AppointmentResponse;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentResponseForCalendar;
import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDay;
import epicode.it.healthdesk.entities.calendar.opening_day.dto.OpeningDayResponse;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CalendarResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private List<OpeningDayResponse> days;
    private List<AppointmentResponseForCalendar> appointments;
    private Boolean isActive;
    private Boolean onHoliday;
    private LocalDate holidayDateStart;
    private LocalDate holidayDateEnd;
}
