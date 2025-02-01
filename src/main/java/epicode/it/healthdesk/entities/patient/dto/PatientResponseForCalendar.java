package epicode.it.healthdesk.entities.patient.dto;

import lombok.Data;

@Data
public class PatientResponseForCalendar {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String taxId;
    private String avatar;
    private String phoneNumber;
}
