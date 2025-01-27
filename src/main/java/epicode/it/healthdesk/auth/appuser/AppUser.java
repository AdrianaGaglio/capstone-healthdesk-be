package epicode.it.healthdesk.auth.appuser;

import epicode.it.healthdesk.entities.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name="appusers")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "appuser_roles", joinColumns = @JoinColumn(name = "appuser_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}