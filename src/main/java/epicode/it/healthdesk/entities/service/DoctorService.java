package epicode.it.healthdesk.entities.service;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.javafaker.Bool;
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

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private Boolean online;

    @ManyToOne
    @JsonBackReference
    private Doctor doctor;

    private Boolean isActive;
}