package epicode.it.healthdesk.auth.appuser;


import epicode.it.healthdesk.auth.dto.AuthResponse;
import epicode.it.healthdesk.auth.dto.LoginRequest;
import epicode.it.healthdesk.auth.dto.RegisterRequest;
import epicode.it.healthdesk.entities.patient.dto.PatientMapper;
import epicode.it.healthdesk.entities.patient.dto.PatientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AppUserSvc appUserSvc;
    private final PatientMapper patientMapper;

    @PostMapping("/register")
    public ResponseEntity<PatientResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(patientMapper.fromPatientToPatientResponse(appUserSvc.registerPatient(registerRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = appUserSvc.authenticateUser(loginRequest);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
