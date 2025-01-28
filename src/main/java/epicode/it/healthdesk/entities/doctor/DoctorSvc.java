package epicode.it.healthdesk.entities.doctor;

import jakarta.persistence.EntityNotFoundException;
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
}
