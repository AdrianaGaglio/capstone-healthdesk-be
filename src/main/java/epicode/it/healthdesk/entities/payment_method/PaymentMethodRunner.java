package epicode.it.healthdesk.entities.payment_method;

import epicode.it.healthdesk.entities.payment_method.dto.PaymentMethodRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@RequiredArgsConstructor
public class PaymentMethodRunner implements ApplicationRunner {
    private final PaymentMethodSvc paymentMethodSvc;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (!paymentMethodSvc.existByName("Carta di credito")) {
            PaymentMethodRequest carta = new PaymentMethodRequest();
            carta.setName("Carta di credito");
            carta.setDescription("Carta di credito");
            carta.setCategory("ELECTRONIC");
            carta.setPriority(1);
            try {
                paymentMethodSvc.create(carta);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }

        if (!paymentMethodSvc.existByName("Bonifico bancario")) {
            PaymentMethodRequest bonifico = new PaymentMethodRequest();
            bonifico.setName("Bonifico bancario");
            bonifico.setDescription("Bonifico bancario");
            bonifico.setCategory("BANKING");
            bonifico.setPriority(2);
            try {
                paymentMethodSvc.create(bonifico);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }

        if (!paymentMethodSvc.existByName("PayPal")) {
            PaymentMethodRequest paypal = new PaymentMethodRequest();
            paypal.setName("PayPal");
            paypal.setDescription("PayPal");
            paypal.setCategory("ELECTRONIC");
            paypal.setPriority(3);
            try {
                paymentMethodSvc.create(paypal);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }

        if (!paymentMethodSvc.existByName("Contanti")) {
            PaymentMethodRequest contanti = new PaymentMethodRequest();
            contanti.setName("Contanti");
            contanti.setDescription("Contanti");
            contanti.setCategory("PHISICAL");
            contanti.setPriority(4);
            try {
                paymentMethodSvc.create(contanti);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }


    }

}
