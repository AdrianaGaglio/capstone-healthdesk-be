package epicode.it.healthdesk.entities.medial_folder.dto;

import epicode.it.healthdesk.entities.appointment.dto.AppointmentResponse;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentResponseForMedicalFolder;
import epicode.it.healthdesk.entities.prescription.Prescription;
import epicode.it.healthdesk.entities.reminder.Reminder;
import lombok.Data;

import java.util.List;

@Data
public class MedicalFolderResponse {
    private Long id;
    private List<AppointmentResponseForMedicalFolder> appointments;
    private List<Prescription> prescriptions;
    private List<Reminder> reminders;
}
