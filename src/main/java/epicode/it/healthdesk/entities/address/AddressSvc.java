package epicode.it.healthdesk.entities.address;

import epicode.it.healthdesk.entities.address.city.City;
import epicode.it.healthdesk.entities.address.city.CitySvc;
import epicode.it.healthdesk.entities.address.dto.AddressMapper;
import epicode.it.healthdesk.entities.address.dto.AddressRequest;
import epicode.it.healthdesk.entities.appointment.Appointment;
import epicode.it.healthdesk.entities.appointment.AppointmentRepo;
import epicode.it.healthdesk.entities.appointment.AppointmentStatus;
import epicode.it.healthdesk.entities.appointment.AppointmentSvc;
import epicode.it.healthdesk.entities.doctor.Doctor;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AddressSvc {
    private final AddressRepo addressRepo;
    private final CitySvc citySvc;
    private final AppointmentRepo appointmentRepo;
    private final AddressMapper mapper;

    // metodo per creare un nuovo indirizzo e salvarlo a DB
    public Address create(@Valid AddressRequest request) {
        Address a = new Address();
        BeanUtils.copyProperties(request, a);
        return addressRepo.save(mapper.toAddress(request));
    }

    // cerco indirizzo per id (questo per lanciare l'eccezione)
    public Address getById(Long id) {
        return addressRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Indirizzo non trovato"));
    }

    // cerco indirizzo per id (questo per utilizzarlo in altri metodi se devo controllare il valore null)
    public Address findById(Long id) {
        return addressRepo.findById(id).orElse(null);
    }


    // cancello l'indirizzo dal db
    @Transactional
    public void delete(Long id) {
        Address a = getById(id);

        // controllo se ci sono appuntamenti prenotati per questo indirizzo
        List<Appointment> appointments = appointmentRepo.findByDoctorAddress(a).stream().filter(a1 -> a1.getStatus() != AppointmentStatus.CANCELLED).toList();
        ;
        // se ci sono lancio l'eccezione
        if (appointments.size() > 0)
            throw new IllegalArgumentException("Ci sono appuntamenti prenotati a questo indirizzo, impossibile cancellare");

        // prendo tutti gli appuntamenti cancellati per questo indirizzo
        List<Appointment> cancelled = appointmentRepo.findByDoctorAddress(a).stream().filter(a1 -> a1.getStatus() == AppointmentStatus.CANCELLED).toList();

        // se ce ne sono, imposto l'indirizzo a null
        if (cancelled.size() > 0)
            cancelled.forEach(app -> app.setDoctorAddress(null));

        addressRepo.delete(a);
    }

    // modifico l'indirizzo
    public Address update(Long id, Address request) {
        Address a = getById(id);
        BeanUtils.copyProperties(request, a);
        return addressRepo.save(a);
    }


}
