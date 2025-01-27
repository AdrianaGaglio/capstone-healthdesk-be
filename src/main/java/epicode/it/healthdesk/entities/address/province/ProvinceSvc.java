package epicode.it.healthdesk.entities.address.province;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import epicode.it.healthdesk.entities.address.province.dto.ProvinceDTO;
import epicode.it.healthdesk.entities.address.province.dto.ProvinceMapper;
import epicode.it.healthdesk.entities.address.province.dto.ProvinceServerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceSvc {
    private final ProvinceRepo provinceRepo;
    private final ProvinceMapper mapper;

    public int count() {
        return (int) provinceRepo.count();
    }

    public List<Province> findAll() {
        return provinceRepo.findAll();
    }

    public List<ProvinceDTO> getProvinces() {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://axqvoqvbfjpaamphztgd.functions.supabase.co/province"))
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
            return mapper.toProvinceDTOList(objectMapper.readValue(response.body(), new TypeReference<List<ProvinceServerResponse>>() {
            }));

        } catch (IOException e) {
            throw new RuntimeException("Errore durante il parsing del JSON", e);
        }
    }

    public void saveProvinces() {
        List<Province> newProvinces = mapper.toProvinceList(getProvinces());
        provinceRepo.saveAll(newProvinces);
    }

    public Province getByAcronym(String acronym) {
        return provinceRepo.findByAcronym(acronym);
    }
}
