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
import org.springframework.http.HttpStatus;
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

    // ❗TO DO: controllo sulla configurazione del db (da rivedere)
    @GetMapping("/count")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Map<String, Boolean>> count() {
        Map<String, Boolean> response = new HashMap<>();
        response.put("configured", appUserSvc.count() > 0);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/check-db")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Map<String, Boolean>> checkDb() {
        Map<String, Boolean> response = new HashMap<>();
        response.put("configured", appUserSvc.count() > 0 && doctorSvc.count() > 0);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<PatientResponse> register(@RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(patientMapper.fromPatientToPatientResponse(appUserSvc.registerPatient(registerRequest)), HttpStatus.CREATED);
    }

    @PostMapping("/new-doctor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponse> registerDoctor(@RequestBody RegisterDoctorRequest registerRequest) {
        if (doctorSvc.count() > 0) throw new IllegalArgumentException("Non possono essere registrati altri medici");
        return new ResponseEntity<>(doctorMapper.fromDoctorToDoctorResponse(appUserSvc.registerDoctor(registerRequest)), HttpStatus.CREATED);
    }

    @PostMapping("/new-admin")
    // ❗TO DO: controllo sull'admin (si può registrare inserendo un codice) - da rivedere l'emissione del codice
    public ResponseEntity<Map<String, String>> registerAdmin(@RequestBody RegisterRequest registerRequest) {
        appUserSvc.registerAdmin(registerRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Amministratore registrato con successo");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = appUserSvc.authenticateUser(loginRequest);

        return ResponseEntity.ok(new AuthResponse(token, jwtTokenUtil.getRolesFromToken(token)));
    }

    @PutMapping("/update")
    @CrossOrigin(   )
    public ResponseEntity<?> updateLoginInfo(@RequestBody AuthUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(request);
        AppUser appUser = appUserSvc.loadUserByEmail(userDetails.getUsername());
        appUser = appUserSvc.updateLoginInfo(appUser, request);
        String token = jwtTokenUtil.generateAccessToken(appUser); // genero un nuovo token dopo la modifica dei dati di accesso
        return new ResponseEntity<>(new AuthResponse(token, jwtTokenUtil.getRolesFromToken(token)), HttpStatus.ACCEPTED);
    }

    // metodo per impostare una nuova password dopo la creazione di un nuovo utente
    @PostMapping("/new-password")
    public ResponseEntity<AuthResponse> newPassword(@RequestBody ResetPassword request) {
        return ResponseEntity.ok(appUserSvc.resetPassword(request));
    }

    // richiesta link per reimpostare la password
    @PostMapping("/reset-request")
    public ResponseEntity<Map<String, String>> resetPasswordRequest(@RequestParam String email) {
        Map<String, String> response = new HashMap<>();
        response.put("message", appUserSvc.resetPasswordRequest(email, false));
        return ResponseEntity.ok(response);
    }
}
