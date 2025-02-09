package epicode.it.healthdesk.entities.document.prescription.dto;

import epicode.it.healthdesk.entities.document.prescription.Prescription;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrescriptionMapper {


    public PrescriptionResponse toPrescriptionResponse(Prescription p) {
        PrescriptionResponse response = new PrescriptionResponse();
        BeanUtils.copyProperties(p, response);
        return response;
    }

    public List<PrescriptionResponse> toPrescriptionResponseList(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toPrescriptionResponse).toList();
    }
}

