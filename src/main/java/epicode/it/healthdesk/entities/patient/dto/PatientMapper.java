package epicode.it.healthdesk.entities.patient.dto;

import epicode.it.healthdesk.entities.address.dto.AddressMapper;
import epicode.it.healthdesk.entities.address.dto.AddressResponse;
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

    private ModelMapper mapper = new ModelMapper();

    public Patient fromPatientRequestToPatient(PatientRequest request) {
        Patient p = new Patient();
        BeanUtils.copyProperties(request, p);
        p.setCreationDate(LocalDate.now());
        return p;
    }

    public Page<PatientResponse> fromPatientPagedToPatientResponsePaged(Page<Patient> pagedPatients) {
        return pagedPatients.map(this::fromPatientToPatientResponse);
    }

    public PatientResponse fromPatientToPatientResponse(Patient p) {
        PatientResponse response = mapper.map(p, PatientResponse.class);
        response.setEmail(p.getAppUser().getEmail());
        response.setAddress(addressMapper.fromAddressToAddressResponse(p.getAddress()));
        return response;
    }

    public List<PatientResponse> fromPatientToPatientResponseList(List<Patient> patients) {
        return patients.stream().map(this::fromPatientToPatientResponse).toList();
    }
}
