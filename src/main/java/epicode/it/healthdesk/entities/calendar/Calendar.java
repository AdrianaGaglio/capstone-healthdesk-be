package epicode.it.healthdesk.entities.calendar;


import com.fasterxml.jackson.annotation.JsonBackReference;
import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDay;
import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "calendars")
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "doctor_id")
    @JsonBackReference
    private Doctor doctor;

    @OneToMany(mappedBy = "calendar")
    private List<OpeningDay> days = new ArrayList<>();

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @Column(name="is_active")
    private Boolean isActive;

    private Boolean onHoliday;

    private LocalDate holidayDateStart;

    private LocalDate holidayDateEnd;

}