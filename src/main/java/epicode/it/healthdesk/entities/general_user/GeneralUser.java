package epicode.it.healthdesk.entities.general_user;

import epicode.it.healthdesk.auth.appuser.AppUser;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "general_users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class GeneralUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String surname;
    private String avatar;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private AppUser appUser;

    @Column(name="last_seen_online")
    private LocalDate lastSeenOnline;
}
