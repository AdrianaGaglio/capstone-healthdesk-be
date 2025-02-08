package epicode.it.healthdesk.entities.patient;

import epicode.it.healthdesk.entities.doctor.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepo extends JpaRepository<Patient, Long> {

    public Optional<Patient> findByTaxId(String taxId);
    public boolean existsByTaxId(String taxId);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.appUser.email) = LOWER(:email)")
    public Optional<Patient> findFirstByEmail(@Param("email") String email);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.name) LIKE(LOWER(CONCAT(:identifier, '%'))) OR LOWER(p.surname) LIKE(LOWER(CONCAT(:identifier, '%')))")
    public Page<Patient> findByNameOrSurname(@Param("identifier") String identifier, Pageable pageable);

    @Query("SELECT d.patients FROM Doctor d WHERE d.id = :doctorId")
    Page<Patient> findAllByDoctor(@Param("doctorId") Long doctorId, Pageable pageable);

    @Query("SELECT p FROM Doctor d JOIN d.patients p " +
           "WHERE d.id = :doctorId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT(:identifier, '%')) " +
           "OR LOWER(p.surname) LIKE LOWER(CONCAT(:identifier, '%')))")
    Page<Patient> findAllByDoctorAndNameOrSurname(@Param("doctorId") Long doctorId,
                                                  @Param("identifier") String identifier,
                                                  Pageable pageable);


}
