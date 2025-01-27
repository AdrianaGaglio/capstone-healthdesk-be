package epicode.it.healthdesk.entities.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="patients")
public class Patient extends User {



}