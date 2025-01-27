package epicode.it.healthdesk.entities.patient;

import epicode.it.healthdesk.entities.addresses.Address;
import epicode.it.healthdesk.entities.general_user.GeneralUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "patients")
public class Patient extends GeneralUser {

    @Column(name = "tax_id", nullable = false, unique = true)
    private String taxId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    private LocalDate creationDate;
}