package epicode.it.healthdesk.entities.service;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


}