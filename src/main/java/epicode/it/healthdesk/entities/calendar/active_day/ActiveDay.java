package epicode.it.healthdesk.entities.calendar.active_day;

import epicode.it.healthdesk.entities.calendar.Calendar;

import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
@Table(name = "active_days")
public class ActiveDay {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private Calendar calendar;

    @Column(name = "day_name")
    @Enumerated(EnumType.STRING)
    private DayName dayName;

//    @OneToMany
//    private List<Slot> slots = new ArrayList<>();

}