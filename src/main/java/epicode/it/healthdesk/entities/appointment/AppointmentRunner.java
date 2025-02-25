package epicode.it.healthdesk.entities.appointment;

import com.github.javafaker.Faker;
import epicode.it.healthdesk.entities.address.Address;
import epicode.it.healthdesk.entities.address.AddressSvc;
import epicode.it.healthdesk.entities.appointment.dto.AppointmentRequest;
import epicode.it.healthdesk.entities.doctor.Doctor;
import epicode.it.healthdesk.entities.doctor.DoctorSvc;
import epicode.it.healthdesk.entities.patient.Patient;
import epicode.it.healthdesk.entities.patient.PatientSvc;
import epicode.it.healthdesk.entities.service.DoctorService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Order(10)
public class AppointmentRunner implements ApplicationRunner {
    private final AppointmentSvc appointmentSvc;
    private final PatientSvc patientSvc;
    private final DoctorSvc doctorSvc;
    private final AddressSvc addressSvc;
    private final Faker faker;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<Patient> allPatients = patientSvc.getAll();
        Doctor d = doctorSvc.getByEmail("infohealthdesk@gmail.com");
        List<Appointment> appointments = appointmentSvc.getAll().stream().filter(a -> a.getStartDate().isAfter(LocalDateTime.now())  || a.getStartDate().isEqual(LocalDateTime.now())).toList();
        if (appointments.size() == 0) {


            for (Patient p : allPatients) {

                AppointmentRequest app = new AppointmentRequest();
                app.setPatientId(p.getId());
                app.setDoctorId(d.getId());

                LocalDateTime start = null;

                LocalDate range = LocalDate.now().plusDays(faker.random().nextInt(1, 30)); // creazione appuntamenti futuri
                for (LocalDate y = range; y.isAfter(LocalDate.now()); y = y.minusDays(1)) {
                    if (y.getDayOfWeek() == DayOfWeek.TUESDAY || y.getDayOfWeek() == DayOfWeek.THURSDAY) {
                        start = y.atTime(faker.random().nextInt(16, 19), 0);

                        break;
                    }
                }
                if (start == null) {
                    continue; // Salta questo appuntamento e passa al prossimo
                }

                app.setStartDate(start);
                app.setEndDate(app.getStartDate().plusHours(1));
//                    app.setStatus(faker.random().nextInt(2) == 1 ? AppointmentStatus.CONFIRMED : AppointmentStatus.CANCELLED);
                app.setOnline(faker.random().nextInt(1, 2) == 1);
                List<DoctorService> services = d.getServices();
                app.setServiceId(services.get(faker.random().nextInt(services.size())).getId());
                Address address = d.getAddresses().entrySet().iterator().next().getValue();
                if (!app.getOnline()) app.setDoctorAddressId(address.getId());

                UserDetails user = null;

                try {
                    boolean isAvailable = !appointmentSvc.existByStartDateAndEndDate(app.getStartDate(), app.getEndDate());
                    if (isAvailable) {

                        appointmentSvc.create(app, user);
                    }

                } catch (EntityExistsException e) {
                    System.out.println(app.getStartDate());
                } catch (RuntimeException e) {
                    System.out.println(app);
                    throw new RuntimeException(e);
                }

            }

        }

    }
}
