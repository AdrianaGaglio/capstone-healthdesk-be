package epicode.it.healthdesk.entities.document.certificate.dto;

import epicode.it.healthdesk.entities.document.certificate.Certificate;
import epicode.it.healthdesk.entities.document.prescription.Prescription;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CertificateMapper {


    public CertificateResponse toCertificateResponse(Certificate p) {
        CertificateResponse response = new CertificateResponse();
        BeanUtils.copyProperties(p, response);
        return response;
    }

    public List<CertificateResponse> toCertificateResponseList(List<Certificate> prescriptions) {
        return prescriptions.stream().map(this::toCertificateResponse).toList();
    }
}

