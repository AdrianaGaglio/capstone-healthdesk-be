package epicode.it.healthdesk.entities.medial_folder.dto;

import epicode.it.healthdesk.entities.appointment.dto.AppointmentMapper;
import epicode.it.healthdesk.entities.document.certificate.dto.CertificateMapper;
import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import epicode.it.healthdesk.entities.document.prescription.dto.PrescriptionMapper;
import epicode.it.healthdesk.entities.reminder.dto.ReminderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicalFolderMapper {
    private final AppointmentMapper appointmentMapper;
    private final PrescriptionMapper prescriptionMapper;
    private final CertificateMapper certificateMapper;
    private final ReminderMapper reminderMapper;

    @Transactional
    public MedicalFolderResponse toMedicalFolderResponse(MedicalFolder mf) {
        MedicalFolderResponse response = new MedicalFolderResponse();
        response.setId(mf.getId());
        response.setAppointments(appointmentMapper.toAppResponseForMFList(mf.getAppointments()));
        response.setPrescriptions(prescriptionMapper.toPrescriptionResponseList(mf.getPrescriptions()));
        response.setDocumentation(certificateMapper.toCertificateResponseList(mf.getDocumentation()));
        response.setReminders(reminderMapper.toReminderResponseList(mf.getReminders()));
        return response;
    }

}
