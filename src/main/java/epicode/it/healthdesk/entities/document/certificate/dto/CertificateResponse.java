package epicode.it.healthdesk.entities.document.certificate.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CertificateResponse {
    private Long id;
    private LocalDate date;
    private String file;
    private String description;
}
