package epicode.it.healthdesk.entities.specialization.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SpecializationResponse {

    private Long id;

    private String name;

    private String description;

    private LocalDate date;
}
