package epicode.it.healthdesk.auth.dto;

import epicode.it.healthdesk.entities.doctor.dto.DoctorRequest;
import epicode.it.healthdesk.entities.patient.dto.PatientRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterDoctorRequest {
    @NotNull(message = "Email richiesta")
    private String email;

    @NotNull(message = "Password richiesta")
    private String password;

    @NotNull(message = "Dati del medico richiesti")
    private DoctorRequest doctor;
}
