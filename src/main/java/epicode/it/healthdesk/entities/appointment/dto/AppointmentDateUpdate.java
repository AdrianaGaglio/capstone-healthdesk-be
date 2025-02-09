package epicode.it.healthdesk.entities.appointment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppointmentDateUpdate {
    @NotNull(message = "Riferimento appuntamento richiesto")
    private Long id;

    @NotNull(message="Data inizio richiesta")
    private LocalDateTime startDate;

    @NotNull(message="Data fine richiesta")
    private LocalDateTime endDate;
}
