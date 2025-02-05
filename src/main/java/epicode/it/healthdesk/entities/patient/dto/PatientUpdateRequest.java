package epicode.it.healthdesk.entities.patient.dto;

import epicode.it.healthdesk.entities.address.Address;
import epicode.it.healthdesk.entities.address.dto.AddressRequest;
import lombok.Data;

@Data
public class PatientUpdateRequest {
    private Long id;
    private String phoneNumber;
    private String avatar;
    private Address address;
}
