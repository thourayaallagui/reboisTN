package ms_suivi.repository;

import ms_suivi.entity.EtatSante;
import ms_suivi.entity.Suivi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuiviRepository extends JpaRepository<Suivi, Long> {

    @Override
    List<Suivi> findAll();

    @Override
    Optional<Suivi> findById(Long id);

    List<Suivi> findByArbreId(Long arbreId);

    @Override
    Page<Suivi> findAll(Pageable pageable);

    Optional<Suivi> findByArbreIdAndDateSuivi(Long arbreId, java.time.LocalDate dateSuivi);

    List<Suivi> findByDateSuiviBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);

    List<Suivi> findByEtatSante(EtatSante etatSante);
}
