package epicode.it.healthdesk.entities.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import epicode.it.healthdesk.entities.address.city.City;
import epicode.it.healthdesk.entities.address.province.Province;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  Long id;

    private String street;

    @Column(name="street_number")
    private String streetNumber;

    private String provinceAcronym;

    private String city;

    private String additional;

    private String postalCode;
}