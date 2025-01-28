package epicode.it.healthdesk.entities.doctor.dto;

import epicode.it.healthdesk.entities.address.dto.AddressResponse;
import epicode.it.healthdesk.entities.experience.Experience;
import epicode.it.healthdesk.entities.payment_method.PaymentMethod;
import epicode.it.healthdesk.entities.service.Service;
import epicode.it.healthdesk.entities.specialization.Specialization;
import epicode.it.healthdesk.entities.training.Training;
import lombok.Data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DoctorResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String avatar;
    private String licenceNumber;
    private String phoneNumber;
    private Map<String, AddressResponse> addresses = new HashMap<>();
    private List<Specialization> specializations = new ArrayList<>();
    private List<Service> services = new ArrayList<>();
    private List<Experience> experiences = new ArrayList<>();
    private List<Training> trainings = new ArrayList<>();
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
}
