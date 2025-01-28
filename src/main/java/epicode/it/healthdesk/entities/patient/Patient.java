package epicode.it.healthdesk.entities.patient;

import epicode.it.healthdesk.entities.address.Address;
import epicode.it.healthdesk.entities.general_user.GeneralUser;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "patients")
public class Patient extends GeneralUser {

    @Column(name = "tax_id", nullable = false, unique = true)
    private String taxId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name="creation_date", nullable = false)
    private LocalDate creationDate;
}