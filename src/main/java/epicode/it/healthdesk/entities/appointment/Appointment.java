package epicode.it.healthdesk.entities.appointment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.javafaker.Bool;
import epicode.it.healthdesk.entities.address.Address;
import epicode.it.healthdesk.entities.address.dto.AddressResponse;
import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.service.DoctorService;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private DoctorService service;

    @ManyToOne
    @JoinColumn(name="medical_folder_id")
    private MedicalFolder medicalFolder;

    @ManyToOne
    private Calendar calendar;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address doctorAddress;

    private Boolean online;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}