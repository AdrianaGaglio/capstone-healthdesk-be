package epicode.it.healthdesk.entities.appointment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlockedSlotRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long doctorId;
}
