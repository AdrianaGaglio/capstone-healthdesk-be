package epicode.it.healthdesk.entities.patient;

import epicode.it.healthdesk.entities.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PatientRepo extends JpaRepository<Patient, Long> {

    public Optional<Patient> findByTaxId(String taxId);
    public boolean existsByTaxId(String taxId);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.appUser.email) = LOWER(:email)")
    public Optional<Patient> findFirstByEmail(@Param("email") String email);
}
