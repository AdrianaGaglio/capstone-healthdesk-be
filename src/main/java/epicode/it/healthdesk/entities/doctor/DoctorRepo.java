package epicode.it.healthdesk.entities.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DoctorRepo extends JpaRepository<Doctor, Long> {

    @Query("SELECT d FROM Doctor d WHERE LOWER(d.appUser.email) = LOWER(:email)")
    public Optional<Doctor> findFirstByEmail(@Param("email") String email);

    public boolean existsByLicenceNumber(String licenceNumber);
    public Optional<Doctor> findFirstByLicenceNumber(String licenceNumber);
}
