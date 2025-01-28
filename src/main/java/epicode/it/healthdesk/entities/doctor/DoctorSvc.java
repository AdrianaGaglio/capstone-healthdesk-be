package epicode.it.healthdesk.entities.doctor;


import epicode.it.healthdesk.entities.calendar.Calendar;
import epicode.it.healthdesk.entities.doctor.dto.DoctorMapper;
import epicode.it.healthdesk.entities.doctor.dto.DoctorRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class DoctorSvc {
    private final DoctorRepo doctorRepo;
    private final DoctorMapper mapper;

    public List<Doctor> getAll() {
        return doctorRepo.findAll();
    }

    public Page<Doctor> getAllPageable(Pageable pageable) {
        return doctorRepo.findAll(pageable);
    }

    public Doctor getById(Long id) {
        return doctorRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Medico non trovato"));
    }

    public int count() {
        return (int) doctorRepo.count();
    }

    public String delete(Long id) {
        Doctor e = getById(id);
        doctorRepo.delete(e);
        return "Medico eliminato con successo";
    }

    public String delete(Doctor e) {
        Doctor foundDoctor = getById(e.getId());
        doctorRepo.delete(foundDoctor);
        return "Medico eliminato con successo";
    }

    public Doctor getByEmail(String email) {
        return doctorRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Medico non trovato"));
    }

    @Transactional
    public Doctor create(@Valid DoctorRequest request) {
        Doctor d = doctorRepo.save(mapper.fromDoctorRequestToDoctor(request));
        Calendar c = new Calendar();
        c.setDoctor(d);
        d.setCalendar(c);
        return d;
    }
}
