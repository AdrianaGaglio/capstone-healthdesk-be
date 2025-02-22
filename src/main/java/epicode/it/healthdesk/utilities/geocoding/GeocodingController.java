package epicode.it.healthdesk.utilities.geocoding;

import epicode.it.healthdesk.utilities.geocoding.dto.Coordinates;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/geocoding")
public class GeocodingController {
    private final GeocodingSvc geocodingSvc;

    @GetMapping("/api-key")
    public ResponseEntity<Map<String, String>> getApiKey() {
        Map<String, String> response = new HashMap<>();
        response.put("apiKey", geocodingSvc.getApiKey());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/coordinates")
    public ResponseEntity<Coordinates> getCoordinates(@RequestParam String address) {
        return new ResponseEntity<>(geocodingSvc.getCoordinates(address), HttpStatus.OK);
    }
}
