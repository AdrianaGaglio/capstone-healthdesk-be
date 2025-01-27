package epicode.it.healthdesk.entities.addresses.city;

import epicode.it.healthdesk.entities.addresses.province.Province;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="cities")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String code;

    private String name;

    @Column(name="postal_code")
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;

}