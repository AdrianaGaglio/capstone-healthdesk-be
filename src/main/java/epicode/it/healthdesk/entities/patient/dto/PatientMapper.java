package epicode.it.healthdesk.entities.patient.dto;

import epicode.it.healthdesk.entities.patient.Patient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PatientMapper {

    public Patient fromPatientRequestToPatient(PatientRequest request) {
        Patient p = new Patient();
        BeanUtils.copyProperties(request, p);
        p.setCreationDate(LocalDate.now());
        return p;
    }


}
