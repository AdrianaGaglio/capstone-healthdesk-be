package epicode.it.healthdesk.auth.appuser;


import com.github.javafaker.App;
import epicode.it.healthdesk.auth.dto.*;
import epicode.it.healthdesk.auth.jwt.JwtTokenUtil;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorResponse;
import epicode.it.healthdesk.entities.general_user.GeneralUser;
import epicode.it.healthdesk.entities.general_user.GeneralUserRepo;
import epicode.it.healthdesk.entities.patient.dto.PatientMapper;
import epicode.it.healthdesk.entities.patient.dto.PatientResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AppUserSvc appUserSvc;
    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;
    private final DoctorSvc doctorSvc;
    private final GeneralUserRepo generalUserRepo;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/check-db")
    public ResponseEntity<Map<String, Boolean>> count() {
        Map<String, Boolean> response = new HashMap<>();
        response.put("configured", appUserSvc.count() > 0);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<PatientResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(patientMapper.fromPatientToPatientResponse(appUserSvc.registerPatient(registerRequest)));
    }

    @PostMapping("/new-doctor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponse> registerDoctor(@RequestBody RegisterDoctorRequest registerRequest) {
        if (doctorSvc.count() > 0) throw new IllegalArgumentException("Non possono essere registrati altri medici");
        return ResponseEntity.ok(doctorMapper.fromDoctorToDoctorResponse(appUserSvc.registerDoctor(registerRequest)));
    }

    @PostMapping("/new-admin")
    public ResponseEntity<Map<String, String>> registerAdmin(@RequestBody RegisterRequest registerRequest) {
        appUserSvc.registerAdmin(registerRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Amministratore registrato con successo");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = appUserSvc.authenticateUser(loginRequest);

        return ResponseEntity.ok(new AuthResponse(token, jwtTokenUtil.getRolesFromToken(token)));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateLoginInfo(@RequestBody AuthUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        AppUser appUser = appUserSvc.loadUserByEmail(userDetails.getUsername());
        appUser = appUserSvc.updateLoginInfo(appUser, request);
        String token = jwtTokenUtil.generateAccessToken(appUser); // genero un nuovo token dopo la modifica dei dati di accesso
        return ResponseEntity.ok(new AuthResponse(token, jwtTokenUtil.getRolesFromToken(token)));
    }

    @PostMapping("/reset")
    public ResponseEntity<AuthResponse> resetAfterFirstAccess(@RequestBody ResetPassword request) {
        return ResponseEntity.ok(appUserSvc.resetPassword(request));
    }

    @PostMapping("/reset-request")
    public ResponseEntity<Map<String, String>> resetPasswordRequest(@RequestParam String email) {
        Map<String, String> response = new HashMap<>();
        response.put("message", appUserSvc.resetPasswordRequest(email, false));
        return ResponseEntity.ok(response);
    }
}
