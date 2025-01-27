package epicode.it.healthdesk.entities.address;

import epicode.it.healthdesk.entities.address.city.CitySvc;
import epicode.it.healthdesk.entities.address.province.ProvinceSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(2)
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
