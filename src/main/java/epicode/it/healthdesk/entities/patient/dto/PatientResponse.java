package epicode.it.healthdesk.entities.patient.dto;

import epicode.it.healthdesk.entities.address.dto.AddressResponse;
import jdk.jfr.DataAmount;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String avatar;
    private String taxId;
    private String phoneNumber;
    private LocalDate birthDate;
    private LocalDate creationDate;
    private AddressResponse address;
    private LocalDate lastVisit;
}
