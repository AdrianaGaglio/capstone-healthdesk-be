package epicode.it.healthdesk.entities.address;

import epicode.it.healthdesk.entities.address.city.CitySvc;
import epicode.it.healthdesk.entities.address.city.dto.CityDTO;
import epicode.it.healthdesk.entities.address.province.ProvinceSvc;
import epicode.it.healthdesk.entities.address.province.dto.ProvinceDTO;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final ProvinceSvc provinceSvc;
    private final CitySvc citySvc;
    private final AddressSvc addressSvc;
    private final DoctorSvc doctorSvc;
    private final DoctorMapper doctorMapper;

    @GetMapping("/provinces")
    public ResponseEntity<List<ProvinceDTO>> getAllProvinces() {
        return ResponseEntity.ok(provinceSvc.getProvinces());
    }

    @GetMapping("/cities/{province}")
    public ResponseEntity<List<CityDTO>> getCitiesByProvince(@PathVariable String province) {
        return ResponseEntity.ok(citySvc.findByProvince(province));
    }

    @DeleteMapping("/doctor/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponse> deleteDoctorAddress(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        addressSvc.delete(id);

        Doctor d = doctorSvc.getByEmail(userDetails.getUsername());

        return ResponseEntity.ok(doctorMapper.fromDoctorToDoctorResponse(d));

    }


}
