package epicode.it.healthdesk.entities.doctor.dto;

import epicode.it.healthdesk.entities.address.dto.AddressResponseForDoctor;
import epicode.it.healthdesk.entities.experience.dto.ExperienceResponse;
import epicode.it.healthdesk.entities.payment_method.PaymentMethod;
import epicode.it.healthdesk.entities.service.dto.DoctorServiceResponse;
import epicode.it.healthdesk.entities.specialization.dto.SpecializationResponse;
import epicode.it.healthdesk.entities.training.dto.TrainingResponse;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

@Data
public class DoctorResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String avatar;
    private String licenceNumber;
    private String phoneNumber;
    private String title;
    private List<AddressResponseForDoctor> addresses = new ArrayList<>();
    private List<SpecializationResponse> specializations = new ArrayList<>();
    private List<DoctorServiceResponse> services = new ArrayList<>();
    private List<ExperienceResponse> experiences = new ArrayList<>();
    private List<TrainingResponse> trainings = new ArrayList<>();
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
}
