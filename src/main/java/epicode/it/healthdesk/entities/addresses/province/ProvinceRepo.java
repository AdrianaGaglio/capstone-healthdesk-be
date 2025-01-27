package epicode.it.healthdesk.entities.addresses.province;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepo extends JpaRepository<Province, Long> {
    public Province findByAcronym(String acronym);
}
