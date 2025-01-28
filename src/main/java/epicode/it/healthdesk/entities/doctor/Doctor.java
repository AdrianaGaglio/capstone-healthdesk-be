package epicode.it.healthdesk.entities.doctor;

import epicode.it.healthdesk.entities.address.Address;
import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.experience.Experience;
import epicode.it.healthdesk.entities.general_user.GeneralUser;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.payment_method.PaymentMethod;
import epicode.it.healthdesk.entities.service.Service;
import epicode.it.healthdesk.entities.specialization.Specialization;
import epicode.it.healthdesk.entities.training.Training;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "doctors")
public class Doctor extends GeneralUser {
    @Column(name = "licence_number", nullable = false, unique = true)
    public String licenceNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany
    private List<Patient> patients = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Specialization> specializations = new ArrayList<>();

    @OneToMany
    private List<Service> services = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

}