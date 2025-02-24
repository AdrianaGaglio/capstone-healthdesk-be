package epicode.it.healthdesk.entities.note;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@NamedQuery(name = "findAll_Note", query = "SELECT a FROM Note a")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDate date;

    private String title;

    @Column(length = 1000)
    private String description;
}