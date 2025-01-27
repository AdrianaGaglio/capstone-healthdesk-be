package epicode.it.healthdesk.entities.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="doctors")
public class Doctor extends User {



}