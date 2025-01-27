package epicode.it.healthdesk.entities.address.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepo extends JpaRepository<City, Long> {

    @Query("SELECT c FROM City c WHERE LOWER(c.name) LIKE LOWER(CONCAT(:name, '%')) AND c.postalCode = :postalCode")
    City findByNameAndPostalCode(@Param("name") String name, @Param("postalCode") String postalCode);


    @Query("SELECT c FROM City c WHERE c.province.acronym = :provinceAcronym")
    public List<City> findByProvinceAcronym(@Param("provinceAcronym") String provinceAcronym);
}
