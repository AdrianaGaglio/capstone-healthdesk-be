package epicode.it.healthdesk.entities.appointment.dto;

import com.github.javafaker.Bool;
import epicode.it.healthdesk.entities.appointment.AppointmentStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    @NotNull(message = "Data di inizio richiesta")
//    @FutureOrPresent(message = "Data e ora di inizio non valida")
    private LocalDateTime startDate;

    @NotNull(message = "Data di fine richiesta")
    private LocalDateTime endDate;

    @NotNull(message = "Paziente richiesto")
    private Long patientId;

    @NotNull(message = "Servizio richiesto")
    private Long serviceId;

    @NotNull(message = "Medico richiesto")
    private Long doctorId;

    private Long doctorAddressId;

    private Boolean online;

    private AppointmentStatus status;
}
