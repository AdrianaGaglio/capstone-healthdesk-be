package epicode.it.healthdesk.entities.addresses;

import epicode.it.healthdesk.entities.addresses.city.CitySvc;
import epicode.it.healthdesk.entities.addresses.province.ProvinceSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressRunner implements ApplicationRunner {
    private final ProvinceSvc provinceSvc;
    private final CitySvc citySvc;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (provinceSvc.count() == 0) {
            provinceSvc.saveProvinces();
        }

        if (citySvc.count() == 0) {
            citySvc.saveCities();
        }

    }
}
