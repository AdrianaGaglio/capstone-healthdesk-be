package epicode.it.healthdesk.entities.reminder;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


}