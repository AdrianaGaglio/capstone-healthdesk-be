package epicode.it.healthdesk.entities.payment_method;

import epicode.it.healthdesk.entities.payment_method.dto.PaymentMethodRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class PaymentMethodSvc {
    private final PaymentMethodRepo paymentMethodRepo;

    public boolean existByName(String name) {
        return paymentMethodRepo.existsByName(name);
    }

    public PaymentMethod getByName(String name) {
        return paymentMethodRepo.findFirstByName(name);
    }

    public int count() {
        return (int) paymentMethodRepo.count();
    }

    public PaymentMethod create(@Valid PaymentMethodRequest request) {
        PaymentMethod p = new PaymentMethod();
        BeanUtils.copyProperties(request, p);
        p.setCategory(PaymentCategory.valueOf(request.getCategory().toUpperCase()));
        return paymentMethodRepo.save(p);
    }


}
