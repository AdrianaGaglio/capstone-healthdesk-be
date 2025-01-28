package epicode.it.healthdesk.entities.doctor.dto;

import epicode.it.healthdesk.entities.address.AddressSvc;
import epicode.it.healthdesk.entities.address.dto.AddressMapper;
import epicode.it.healthdesk.entities.address.dto.AddressRequest;
import epicode.it.healthdesk.entities.address.dto.AddressResponseForDoctor;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.experience.dto.ExperienceMapper;
import epicode.it.healthdesk.entities.service.dto.DoctorServiceMapper;
import epicode.it.healthdesk.entities.specialization.dto.SpecializationMapper;
import epicode.it.healthdesk.entities.training.dto.TrainingMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DoctorMapper {
    private final AddressSvc addressSvc;
    private final AddressMapper addressMapper;
    private final SpecializationMapper specializationMapper;
    private final DoctorServiceMapper serviceMapper;
    private final ExperienceMapper experienceMapper;
    private final TrainingMapper trainingMapper;



    private ModelMapper mapper = new ModelMapper();


    @Transactional
    public Doctor fromDoctorRequestToDoctor(DoctorRequest request) {
        Doctor d = new Doctor();
        BeanUtils.copyProperties(request, d);
        if (request.getAddresses() == null || request.getAddresses().isEmpty()) {
            throw new IllegalArgumentException("L'utente deve avere almeno un indirizzo.");
        }
        request.getAddresses().forEach(a -> {
            AddressRequest ar = new AddressRequest();
            BeanUtils.copyProperties(a, ar);
            d.getAddresses().put(a.getName(), addressSvc.create(ar));
        });
        return d;
    }

    public DoctorResponse fromDoctorToDoctorResponse(Doctor d) {
        DoctorResponse response = mapper.map(d, DoctorResponse.class);
        response.setEmail(d.getAppUser().getEmail());
        response.setSpecializations(specializationMapper.toSpecializationResponseList(d.getSpecializations()));
        response.setServices(serviceMapper.toDoctorServiceResponseList(d.getServices()));
        response.setExperiences(experienceMapper.toExperienceResponseList(d.getExperiences()));
        response.setTrainings(trainingMapper.toTrainingResponseList(d.getTrainings()));
        response.setPaymentMethods(d.getPaymentMethods());

        d.getAddresses().forEach((name, address) -> {
            AddressResponseForDoctor ar = new AddressResponseForDoctor(name, addressMapper.fromAddressToAddressResponse(address));
            response.getAddresses().add(ar);
        });

        return response;
    }

    public List<DoctorResponse> fromDoctorToDoctorResponseList(List<Doctor> doctors) {
        return doctors.stream().map(this::fromDoctorToDoctorResponse).toList();
    }
}
