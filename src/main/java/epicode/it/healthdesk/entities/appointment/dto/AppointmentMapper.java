package epicode.it.healthdesk.entities.appointment.dto;

import epicode.it.healthdesk.entities.address.dto.AddressMapper;
import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.patient.dto.PatientMapper;
import epicode.it.healthdesk.entities.patient.dto.PatientResponse;
import epicode.it.healthdesk.entities.service.dto.DoctorServiceMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {
    private final DoctorMapper doctorMapper;
    private final PatientMapper patientMapper;
    private final DoctorServiceMapper serviceMapper;
    private final AddressMapper addressMapper;

    private ModelMapper mapper = new ModelMapper();

    public AppointmentResponse toAppointmentResponse(Appointment a) {
        AppointmentResponse response = mapper.map(a, AppointmentResponse.class);
        response.setDoctor(doctorMapper.fromDoctorToDoctorResponse(a.getCalendar().getDoctor()));
        response.setPatient(patientMapper.fromPatientToPatientResponse(a.getMedicalFolder().getPatient()));
        response.setService(serviceMapper.toDoctorServiceResponse(a.getService()));
        response.setStatus(a.getStatus().toString());
        if (a.getDoctorAddress() != null) {
            response.setAddress(addressMapper.fromAddressToAddressResponse(a.getDoctorAddress()));
        }
        return response;
    }

    public List<AppointmentResponse> toAppointmentResponseList(List<Appointment> appointments) {
        return appointments.stream().map(this::toAppointmentResponse).toList();
    }

    public Page<AppointmentResponse> toAppointmentsPaged(Page<Appointment> pagedAppointments) {
        return pagedAppointments.map(this::toAppointmentResponse);
    }

    public AppointmentResponseForCalendar toAppointmentResponseForCalendar(Appointment a) {
        AppointmentResponseForCalendar response = mapper.map(a, AppointmentResponseForCalendar.class);
        response.setService(serviceMapper.toDoctorServiceResponse(a.getService()));
        response.setPatient(patientMapper.toPatientResponseForCalendar(a.getMedicalFolder().getPatient()));
        response.setStatus(a.getStatus().toString());
        if (a.getDoctorAddress() != null) {
            response.setAddress(addressMapper.fromAddressToAddressResponse(a.getDoctorAddress()));
        }
        return response;
    }

    public List<AppointmentResponseForCalendar> toAppointmentResponseForCalendarList(List<Appointment> appointments) {
        return appointments.stream().map(this::toAppointmentResponseForCalendar).toList();
    }

    public AppointmentResponseForPatient toAppointmentResponseForPatient(Appointment a) {
        AppointmentResponseForPatient response = mapper.map(a, AppointmentResponseForPatient.class);
        response.setPatient(null);
        response.setService(null);
        response.setStatus(a.getStatus().toString());
        if (a.getDoctorAddress() != null) {
            response.setAddress(addressMapper.fromAddressToAddressResponse(a.getDoctorAddress()));
        }
        return response;
    }

    public List<AppointmentResponseForPatient> toAppointmentResponseForPatientList(List<Appointment> appointments) {
        return appointments.stream().map(this::toAppointmentResponseForPatient).toList();
    }

    public AppointmentResponseForMedicalFolder toAppResponseForMF(Appointment a) {
        AppointmentResponseForMedicalFolder response = mapper.map(a, AppointmentResponseForMedicalFolder.class);
        response.setService(serviceMapper.toDoctorServiceResponse(a.getService()));
        if (a.getDoctorAddress() != null) {
            response.setAddress(addressMapper.fromAddressToAddressResponse(a.getDoctorAddress()));
        }
        response.setDoctor(doctorMapper.fromDoctorToDoctorResponse(a.getCalendar().getDoctor()));
        return response;
    }

    public List<AppointmentResponseForMedicalFolder> toAppResponseForMFList(List<Appointment> appointments) {
        return appointments.stream().map(this::toAppResponseForMF).toList();
    }
}


