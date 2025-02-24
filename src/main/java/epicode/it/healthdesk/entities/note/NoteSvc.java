package epicode.it.healthdesk.entities.note;

import epicode.it.healthdesk.entities.medial_folder.MedicalFolder;
import epicode.it.healthdesk.entities.note.dto.NoteRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class NoteSvc {
    private final NoteRepo noteRepo;

    public List<Note> getAll() {
        return noteRepo.findAll();
    }

    public Page<Note> getAllPageable(Pageable pageable) {
        return noteRepo.findAll(pageable);
    }

    public Note getById(Long id) {
        return noteRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Nessuna nota trovata"));
    }

    public int count() {
        return (int) noteRepo.count();
    }

    public String delete(Long id) {
        Note e = getById(id);
        noteRepo.delete(e);
        return "Nota cancellata correttamente";
    }

    public String delete(Note e) {
        Note foundNote = getById(e.getId());
        noteRepo.delete(foundNote);
        return "Nota cancellata correttamente";
    }

    public Note create(@Valid NoteRequest request) {
        Note n = new Note();
        BeanUtils.copyProperties(request, n);
        n.setDate(LocalDate.now());
        return noteRepo.save(n);
    }
}
