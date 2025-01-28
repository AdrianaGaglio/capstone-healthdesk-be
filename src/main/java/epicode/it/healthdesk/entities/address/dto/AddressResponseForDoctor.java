package epicode.it.healthdesk.entities.address.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressResponseForDoctor {
    private String name;
    private AddressResponse address;
}
