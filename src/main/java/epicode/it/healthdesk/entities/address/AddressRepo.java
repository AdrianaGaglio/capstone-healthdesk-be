package epicode.it.healthdesk.entities.address;

import epicode.it.healthdesk.entities.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address, Long> {


}
