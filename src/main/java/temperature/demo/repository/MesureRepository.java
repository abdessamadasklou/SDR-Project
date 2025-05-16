package temperature.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import temperature.demo.model.Mesure;

import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface MesureRepository extends JpaRepository<Mesure, Long> {

    @Query("SELECT DISTINCT m.capteurName FROM Mesure m")
    List<String> findDistinctCapteurNames();

    List<Mesure> findByCapteurNameOrderByInsertionTimeAsc(String capteurName);
}
