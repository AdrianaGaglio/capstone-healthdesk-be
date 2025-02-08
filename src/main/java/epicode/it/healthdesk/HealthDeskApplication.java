package epicode.it.healthdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HealthDeskApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthDeskApplication.class, args);
    }

}
