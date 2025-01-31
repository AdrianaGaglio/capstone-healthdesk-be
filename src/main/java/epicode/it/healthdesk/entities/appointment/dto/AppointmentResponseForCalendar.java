package epicode.it.healthdesk.entities.appointment.dto;

import epicode.it.healthdesk.entities.address.dto.AddressResponse;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.patient.dto.PatientResponseForCalendar;
import epicode.it.healthdesk.entities.service.dto.DoctorServiceResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponseForCalendar {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private DoctorServiceResponse service;
    private PatientResponseForCalendar patient;
    private String status;
    private AddressResponse address;
}
