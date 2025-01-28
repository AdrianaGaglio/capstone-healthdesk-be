package epicode.it.healthdesk.entities.payment_method.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentMethodRequest {
    @NotNull(message = "Nome metodo di pagamento richiesto")
    private String name;
    private String description;

    @NotNull(message = "Categoria metodo di pagamento richiesta")
    private String category;

    private int priority = 100;
}
