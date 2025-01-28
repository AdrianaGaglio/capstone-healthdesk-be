package epicode.it.healthdesk.entities.doctor.dto;

import epicode.it.healthdesk.entities.address.AddressSvc;
import epicode.it.healthdesk.entities.address.dto.AddressRequest;
import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorMapper {
    private final AddressSvc addressSvc;

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
}
