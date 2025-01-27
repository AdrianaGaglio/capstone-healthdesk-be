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
    private final ProvinceSvc provinceSvc;
    private final CityMapper mapper;

    public int count() {
        return (int) cityRepo.count();
    }

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

    @Transactional
    public void saveCities() {
        List<CityDTO> newCities = getCities();
        for (int i = 0; i < newCities.size(); i++) {
            City newCity = mapper.toCity(newCities.get(i));
            newCity.setProvince(provinceSvc.getByAcronym(newCities.get(i).getProvinceAcronym()));
            cityRepo.save(newCity);
        }
    }

    public City findByNameAndPostalCode(String name, String postalCode) {
        return cityRepo.findByNameAndPostalCode(name, postalCode);
    }

    public List<City> findByProvinceAcronym(String provinceAcronym) {
        return cityRepo.findByProvinceAcronym(provinceAcronym);
    }
}
