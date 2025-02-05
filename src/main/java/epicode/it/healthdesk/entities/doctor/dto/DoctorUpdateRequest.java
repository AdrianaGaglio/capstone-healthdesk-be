package epicode.it.healthdesk.entities.doctor.dto;

import lombok.Data;

@Data
public class DoctorUpdateRequest {
    private Long id;
    private String name;
    private String title;
    private String avatar;
    private String licenceNumber;
    private String phoneNumber;
}
