package epicode.it.healthdesk.entities.doctor.dto;

import epicode.it.healthdesk.entities.address.AddressSvc;
import epicode.it.healthdesk.entities.address.dto.AddressMapper;
import epicode.it.healthdesk.entities.address.dto.AddressRequest;
import epicode.it.healthdesk.entities.address.dto.AddressResponse;
import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component

public class DoctorMapper {

    @Autowired
    private AddressSvc addressSvc;

    @Autowired
    private AddressMapper addressMapper;

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
        response.setSpecializations(d.getSpecializations());
        response.setServices(d.getServices());
        response.setExperiences(d.getExperiences());
        response.setTrainings(d.getTrainings());
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
