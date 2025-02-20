package epicode.it.healthdesk.entities.patient.dto;

import epicode.it.healthdesk.entities.address.AddressSvc;
import epicode.it.healthdesk.entities.address.dto.AddressMapper;
import epicode.it.healthdesk.entities.address.dto.AddressResponse;
import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.appointment.AppointmentSvc;
import epicode.it.healthdesk.entities.patient.Patient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PatientMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private AppointmentSvc appointmentSvc;

    @Autowired
    private AddressSvc addressSvc;

    private ModelMapper mapper = new ModelMapper();

    public Patient fromPatientRequestToPatient(PatientRequest request) {
        Patient p = new Patient();
        BeanUtils.copyProperties(request, p);
        p.setCreationDate(p.getCreationDate() == null ? LocalDate.now() : request.getCreationDate());

        if (request.getTaxId() == null) p.setTaxId(null);

        if (request.getAddress() != null) {
            p.setAddress(addressSvc.create(request.getAddress()));
        } else {
            p.setAddress(null);
        }

        return p;
    }

    public Page<PatientResponse> fromPatientPagedToPatientResponsePaged(Page<Patient> pagedPatients) {
        return pagedPatients.map(this::fromPatientToPatientResponse);
    }

    public PatientResponse fromPatientToPatientResponse(Patient p) {
        PatientResponse response = mapper.map(p, PatientResponse.class);
        response.setEmail(p.getAppUser().getEmail());
        if (p.getAddress() != null) {
            response.setAddress(addressMapper.fromAddressToAddressResponse(p.getAddress()));
        } else {
            response.setAddress(null);
        }
        Appointment lastAppointment = appointmentSvc.findLastByMedicalFolder(p.getId());
        if (lastAppointment != null) {
            response.setLastVisit(appointmentSvc.findLastByMedicalFolder(p.getId()).getStartDate().toLocalDate());
        } else {
            response.setLastVisit(null);
        }

        if(p.getLastSeenOnline() == null) {
            p.setLastSeenOnline(null);
        }
        return response;
    }

    public List<PatientResponse> fromPatientToPatientResponseList(List<Patient> patients) {
        return patients.stream().map(this::fromPatientToPatientResponse).toList();
    }

    public PatientResponseForCalendar toPatientResponseForCalendar(Patient patient) {
        PatientResponseForCalendar response = new PatientResponseForCalendar();
        BeanUtils.copyProperties(patient, response);
        response.setEmail(patient.getAppUser().getEmail());
        return response;
    }
}
