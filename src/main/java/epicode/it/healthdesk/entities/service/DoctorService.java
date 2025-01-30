package epicode.it.healthdesk.entities.service;

import com.fasterxml.jackson.annotation.JsonBackReference;
import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "services")
public class DoctorService {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean online;

    @ManyToOne
    @JsonBackReference
    private Doctor doctor;
}