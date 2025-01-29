package epicode.it.healthdesk.entities.calendar.calendar_setting;

import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.calendar.active_day.ActiveDay;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="calendar_settings")
public class CalendarSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    private Calendar calendar;

    @OneToMany(mappedBy = "settings", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActiveDay> activeDays = new ArrayList<>();

    public CalendarSettings() {}

    public CalendarSettings(Calendar c) {
        this.calendar = c;
    }
}