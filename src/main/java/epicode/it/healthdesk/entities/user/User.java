package epicode.it.healthdesk.entities.user;

import epicode.it.healthdesk.auth.appuser.AppUser;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String surname;
    private String avatar;

    @OneToOne
    @JoinColumn(name = "appuser_id")
    private AppUser appUser;
}