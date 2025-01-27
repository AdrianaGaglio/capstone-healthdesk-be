package epicode.it.healthdesk.entities.address;

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

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
}