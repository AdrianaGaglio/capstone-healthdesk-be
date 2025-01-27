package epicode.it.healthdesk.entities.addresses;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import epicode.it.healthdesk.entities.addresses.province.Province;
import epicode.it.healthdesk.entities.addresses.province.ProvinceRepo;
import epicode.it.healthdesk.entities.addresses.province.dto.ProvinceDTO;
import epicode.it.healthdesk.entities.addresses.province.dto.ProvinceMapper;
import epicode.it.healthdesk.entities.addresses.province.dto.ProvinceServerResponse;
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
public class AddressSvc {



}
