package epicode.it.healthdesk.entities.patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepo extends JpaRepository<Patient, Long> {

    public Optional<Patient> findByTaxId(String taxId);
    public boolean existsByTaxId(String taxId);
}
