package epicode.it.healthdesk.entities.medial_folder;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.prescription.Prescription;
import epicode.it.healthdesk.entities.reminder.Reminder;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "medical_folders")
public class MedicalFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany
    private List<Prescription> prescriptions = new ArrayList<>();

    @OneToMany
    private List<Reminder> reminders = new ArrayList<>();
}