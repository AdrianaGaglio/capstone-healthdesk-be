package epicode.it.healthdesk.utilities.geocoding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import epicode.it.healthdesk.utilities.geocoding.dto.Coordinates;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class GeocodingSvc {

    String apiUrl = "https://nominatim.openstreetmap.org/search";

    @Autowired
    @Value("${spring.googleMap.apiKey}")
    private String apiKey;

    public Coordinates getCoordinates(String address) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String url = apiUrl + "?q=" + encodedAddress + "&format=json&limit=1";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            if (root.isArray() && root.size() > 0) {
                JsonNode firstResult = root.get(0);
                double lat = firstResult.get("lat").asDouble();
                double lng = firstResult.get("lon").asDouble();
                return new Coordinates(lat, lng);
            } else {
                return null; // Indirizzo non trovato
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante la chiamata HTTP", e);
        }
    }

    public String getApiKey() {
        return apiKey;
    }
}
