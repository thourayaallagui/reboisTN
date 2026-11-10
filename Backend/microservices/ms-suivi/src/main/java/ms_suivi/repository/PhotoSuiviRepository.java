package ms_suivi.repository;

import ms_suivi.entity.PhotoSuivi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoSuiviRepository extends JpaRepository<PhotoSuivi, Long> {

    List<PhotoSuivi> findBySuiviId(Long suiviId);

    List<PhotoSuivi> findByDateAjoutBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);
}
