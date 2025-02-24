package epicode.it.healthdesk.entities.medial_folder.dto;

import epicode.it.healthdesk.entities.appointment.dto.AppointmentResponseForMedicalFolder;
import epicode.it.healthdesk.entities.document.certificate.dto.CertificateResponse;
import epicode.it.healthdesk.entities.document.prescription.dto.PrescriptionResponse;
import epicode.it.healthdesk.entities.note.Note;
import epicode.it.healthdesk.entities.reminder.dto.ReminderResponse;
import lombok.Data;

import java.util.List;

@Data
public class MedicalFolderResponseForPatient {
    private Long id;
    private List<AppointmentResponseForMedicalFolder> appointments;
    private List<PrescriptionResponse> prescriptions;
    private List<CertificateResponse> documentation;
    private List<ReminderResponse> reminders;
}
