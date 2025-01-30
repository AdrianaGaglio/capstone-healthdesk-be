package epicode.it.healthdesk.entities.doctor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import epicode.it.healthdesk.entities.address.Address;
import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.experience.Experience;
import epicode.it.healthdesk.entities.general_user.GeneralUser;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.payment_method.PaymentMethod;
import epicode.it.healthdesk.entities.service.DoctorService;
import epicode.it.healthdesk.entities.specialization.Specialization;
import epicode.it.healthdesk.entities.training.Training;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "doctors")
public class Doctor extends GeneralUser {
    @Column(name = "licence_number", nullable = false, unique = true)
    public String licenceNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyColumn(name = "address_name")
    @JoinColumn(name = "doctor_id")
    @ToString.Exclude
    private Map<String, Address> addresses = new HashMap<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Patient> patients = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Specialization> specializations = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DoctorService> services = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "calendar_id")
    @JsonManagedReference
    private Calendar calendar;

    private String title;

    @OneToMany
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

}