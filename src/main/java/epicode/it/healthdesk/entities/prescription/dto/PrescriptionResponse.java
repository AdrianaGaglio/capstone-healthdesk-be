package epicode.it.healthdesk.entities.prescription.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PrescriptionResponse {
    private Long id;
    private LocalDate date;
    private String file;
}
