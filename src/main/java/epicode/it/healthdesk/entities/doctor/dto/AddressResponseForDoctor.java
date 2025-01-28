package epicode.it.healthdesk.entities.doctor.dto;

import epicode.it.healthdesk.entities.address.dto.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressResponseForDoctor {
    private String name;
    private AddressResponse address;
}
