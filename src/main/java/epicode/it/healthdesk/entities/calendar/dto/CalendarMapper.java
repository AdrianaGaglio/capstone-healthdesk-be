package epicode.it.healthdesk.entities.calendar.dto;

import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.appointment.AppointmentStatus;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentMapper;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentResponse;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentResponseForCalendar;
import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.calendar.opening_day.dto.OpeningDayMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CalendarMapper {
    private final AppointmentMapper appointmentMapper;
    private final OpeningDayMapper dayMapper;

    private ModelMapper mapper = new ModelMapper();


    public CalendarResponse toCalendarResponse(Calendar c) {
        CalendarResponse response = mapper.map(c, CalendarResponse.class);
        response.setDoctorId(c.getDoctor().getId());
        response.setDoctorName(c.getDoctor().getName() + " " + c.getDoctor().getSurname());
        List<Appointment> appointments = c.getAppointments().stream().filter(a -> !a.getStatus().equals(AppointmentStatus.CANCELLED)).toList();
        response.setAppointments(appointmentMapper.toAppointmentResponseForCalendarList(appointments));
        response.setDays(dayMapper.toOpeningDayResponseList(c.getDays()));
        response.setIsActive(c.getIsActive());
        if(c.getOnHoliday() == null) response.setOnHoliday(false);
        return response;
    }

    public CalendarResponseForPatient toCalendarResponseForPatient(Calendar c) {
        CalendarResponseForPatient response = mapper.map(c, CalendarResponseForPatient.class);
        response.setDoctorId(c.getDoctor().getId());
        List<Appointment> appointments = c.getAppointments().stream().filter(a -> !a.getStatus().equals(AppointmentStatus.CANCELLED)).toList();
        response.setAppointments(appointmentMapper.toAppointmentResponseForPatientList(c.getAppointments()));
        response.setDays(dayMapper.toOpeningDayResponseList(c.getDays()));
        response.setIsActive(c.getIsActive());
        return response;
    }
}
