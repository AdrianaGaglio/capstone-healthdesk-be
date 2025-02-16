package epicode.it.healthdesk.auth.appuser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodeRepo extends JpaRepository<AuthCode, Long> {

    AuthCode findByEmail(String email);
}
