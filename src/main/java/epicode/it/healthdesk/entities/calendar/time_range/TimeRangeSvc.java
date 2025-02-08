package epicode.it.healthdesk.entities.calendar.time_range;

import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDay;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeRangeSvc {
    private final TimeRangeRepo timeRangeRepo;

    public List<TimeRange> getAll() {
        return timeRangeRepo.findAll();
    }

    public Page<TimeRange> getAllPageable(Pageable pageable) {
        return timeRangeRepo.findAll(pageable);
    }

    public TimeRange getById(Long id) {
        return timeRangeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Range orario non trovato"));
    }

    public boolean existsByOpeningDay(OpeningDay day) {
        return timeRangeRepo.existsByOpeningDay(day);
    }
    public TimeRange findByOpeningDay(OpeningDay day) {
        return timeRangeRepo.findByOpeningDay(day);
    }

    public int count() {
        return (int) timeRangeRepo.count();
    }

    public String delete(Long id) {
        TimeRange e = getById(id);
        timeRangeRepo.delete(e);
        return "Range orario eliminato";
    }

    public String delete(TimeRange e) {
        TimeRange foundTimeRange = getById(e.getId());
        timeRangeRepo.delete(foundTimeRange);
        return "Range orario eliminato";
    }

    @Transactional
    public TimeRange add(OpeningDay day, LocalTime startTime, LocalTime endTime) {
        TimeRange tr = new TimeRange();
        tr.setStartTime(startTime);
        tr.setEndTime(endTime);
        tr.setOpeningDay(day);
        return timeRangeRepo.save(tr);
    }
}
