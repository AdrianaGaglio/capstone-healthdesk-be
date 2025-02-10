package epicode.it.healthdesk.auth.appuser;


import com.github.javafaker.App;
import epicode.it.healthdesk.auth.dto.*;
import epicode.it.healthdesk.auth.jwt.JwtTokenUtil;
import epicode.it.healthdesk.entities.general_user.GeneralUser;
import epicode.it.healthdesk.entities.general_user.GeneralUserRepo;
import epicode.it.healthdesk.entities.patient.dto.PatientMapper;
import epicode.it.healthdesk.entities.patient.dto.PatientResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final GeneralUserRepo generalUserRepo;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<PatientResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(patientMapper.fromPatientToPatientResponse(appUserSvc.registerPatient(registerRequest)));
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
}
