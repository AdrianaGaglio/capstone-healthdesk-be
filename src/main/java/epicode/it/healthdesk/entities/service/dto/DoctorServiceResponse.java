package epicode.it.healthdesk.entities.service.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class DoctorServiceResponse {
    private Long id;

    private String name;

    private String description;

    private double price;

    private Boolean online;

    private Boolean isActive;
}
