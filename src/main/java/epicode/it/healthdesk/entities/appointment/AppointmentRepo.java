package epicode.it.healthdesk.entities.appointment;

import epicode.it.healthdesk.entities.address.Address;
import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.service.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    // cerco il primo appuntamento per calendario e data inizio appuntamento
    @Query("SELECT a From Appointment a WHERE a.calendar.id = :calendarId AND a.startDate = :startDate")
    public Optional<Appointment> findFirstByCalendarIdAndStartDate(@Param("calendarId") Long calendarId, @Param("startDate") LocalDateTime startDate);

    // cerco gli appuntamenti per servizio prenotato
    public List<Appointment> findByService(DoctorService service);

    // pageable per calendario e data successiva a quella specificata
    Page<Appointment> findByCalendarAndStartDateAfter(Calendar calendar, LocalDateTime date, Pageable pageable);

    // cerco il primo appuntamento per data inizio e fine
    public Appointment findFirstByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate);

    // controllo se esiste un appuntamento per un preciso slot
    public boolean existsByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate);

    // cerco la lista di ultimi appuntamenti per medical folder, con stato confermato e data di fine inferiore a quella corrente
    @Query("SELECT a From Appointment a WHERE a.medicalFolder.id = :id AND a.status = 'CONFIRMED' AND  a.endDate < CURRENT_TIMESTAMP ORDER BY a.startDate DESC")
    public List<Appointment> findLastByMedicalFolder(@Param("id") Long id);

    // cerco per indirizzo del medico
    public List<Appointment> findByDoctorAddress(Address address);

    // cerco tutti gli appuntamenti con stato pending che iniziano entro un preciso range di orari (serve per schedulare l'invio di mail di richiesta conferma automatico)
    @Query("SELECT a FROM Appointment a WHERE a.status = 'PENDING' AND a.startDate BETWEEN :startOfDay AND :endOfDay")
    List<Appointment> findAllBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);


    List<Appointment> findAllByStartDateBetween(LocalDate start, LocalDate end);

}

