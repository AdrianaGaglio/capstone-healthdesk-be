package epicode.it.healthdesk.entities.appointment.dto;

import epicode.it.healthdesk.entities.address.dto.AddressMapper;
import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.patient.dto.PatientMapper;
import epicode.it.healthdesk.entities.service.dto.DoctorServiceMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
        response.setAddress(addressMapper.fromAddressToAddressResponse(a.getDoctorAddress()));
        return response;
    }

    public List<AppointmentResponse> toAppointmentResponseList(List<Appointment> appointments) {
        return appointments.stream().map(this::toAppointmentResponse).toList();
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
}


