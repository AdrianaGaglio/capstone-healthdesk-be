package epicode.it.healthdesk.entities.service.dto;

import epicode.it.healthdesk.entities.service.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DoctorServiceMapper {
    private ModelMapper mapper = new ModelMapper();

    public DoctorServiceResponse toDoctorServiceResponse(DoctorService ds) {
        return mapper.map(ds, DoctorServiceResponse.class);
    }

    public List<DoctorServiceResponse> toDoctorServiceResponseList(List<DoctorService> services) {
        return services.stream().map(this::toDoctorServiceResponse).toList();
    }
}
