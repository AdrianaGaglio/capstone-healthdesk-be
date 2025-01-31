package epicode.it.healthdesk.entities.address.city;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import epicode.it.healthdesk.entities.address.city.dto.CityDTO;
import epicode.it.healthdesk.entities.address.city.dto.CityMapper;
import epicode.it.healthdesk.entities.address.city.dto.CityServerResponse;
import epicode.it.healthdesk.entities.address.province.ProvinceSvc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitySvc {
    private final CityRepo cityRepo;
    private final CityMapper mapper;


    public List<CityDTO> getCities() {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(java.net.URI.create("https://axqvoqvbfjpaamphztgd.functions.supabase.co/comuni"))
                .GET()
                .build();

        HttpResponse<String> response = null;


        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return mapper.toCityDTOList(objectMapper.readValue(response.body(), new TypeReference<List<CityServerResponse>>() {
            }));
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il parsing del JSON", e);
        }
    }



    public List<CityDTO> findByNameAndPostalCode(String postalCode) {
        return getCities().stream().filter(c -> c.getPostalCode().equals(postalCode)).toList();
    }

    public List<CityDTO> findByProvince(String province) {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(java.net.URI.create("https://axqvoqvbfjpaamphztgd.functions.supabase.co/comuni/provincia/" + province))
                .GET()
                .build();

        HttpResponse<String> response = null;


        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return mapper.toCityDTOList(objectMapper.readValue(response.body(), new TypeReference<List<CityServerResponse>>() {
            }));
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il parsing del JSON", e);
        }
    }
}
