package epicode.it.healthdesk.auth.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {


    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);

}
