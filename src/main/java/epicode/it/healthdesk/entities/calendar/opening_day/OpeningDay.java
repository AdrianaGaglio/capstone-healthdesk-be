package epicode.it.healthdesk.entities.calendar.opening_day;


import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.calendar.time_range.TimeRange;
import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "opening_days")
public class OpeningDay {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "day_name")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayName;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @OneToMany(mappedBy = "openingDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeRange> ranges = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;
}