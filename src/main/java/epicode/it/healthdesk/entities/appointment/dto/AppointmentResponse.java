package epicode.it.healthdesk.entities.appointment.dto;

import epicode.it.healthdesk.entities.address.dto.AddressResponse;
import epicode.it.healthdesk.entities.doctor.dto.DoctorResponse;
import epicode.it.healthdesk.entities.patient.dto.PatientResponse;
import epicode.it.healthdesk.entities.service.dto.DoctorServiceResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponse {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private DoctorResponse doctor;
    private PatientResponse patient;
    private DoctorServiceResponse service;
    private String status;
    private AddressResponse doctorAddress;
    private Boolean online;
}
