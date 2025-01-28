package epicode.it.healthdesk.entities.payment_method;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepo extends JpaRepository<PaymentMethod, Long> {

    public boolean existsByName(String name);

    public PaymentMethod findFirstByName(String name);
}
