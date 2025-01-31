package epicode.it.healthdesk.entities.appointment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    @NotNull(message = "Data di inizio richiesta")
    private LocalDateTime startDate;

    @NotNull(message = "Data di fine richiesta")
    private LocalDateTime endDate;

    @NotNull(message = "Paziente richiesto")
    private Long patientId;

    @NotNull(message = "Servizio richiesto")
    private Long serviceId;

    @NotNull(message = "Medico richiesto")
    private Long doctorId;

    @NotNull(message = "Indirizzo richiesto")
    private Long doctorAddressId;
}
