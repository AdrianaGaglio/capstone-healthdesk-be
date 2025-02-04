package epicode.it.healthdesk.utilities.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FirestoreController {
    private final FirestoreSvc firestoreSvc;

    @PostMapping(path = "/upload-prescription", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<String> uploadPrescription(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = firestoreSvc.uploadPrescription(file);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'upload del file: " + e.getMessage());
        }
    }

    @PostMapping(path = "/upload-image", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = firestoreSvc.uploadPrescription(file);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'upload del file: " + e.getMessage());
        }
    }
}
