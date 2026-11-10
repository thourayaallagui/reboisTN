package ms_suivi.repository;

import ms_suivi.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {

    List<Intervention> findBySuiviId(Long suiviId);

    List<Intervention> findByTypeIntervention(String typeIntervention);

    List<Intervention> findByDateInterventionBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);
}
