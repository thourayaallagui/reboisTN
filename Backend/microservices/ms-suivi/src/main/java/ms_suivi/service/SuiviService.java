package ms_suivi.service;

import ms_suivi.entity.EtatSante;
import ms_suivi.entity.Suivi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SuiviService {

    Suivi createSuivi(Suivi suivi);

    Suivi updateSuivi(Long id, Suivi suivi);

    void deleteSuivi(Long id);

    Optional<Suivi> getSuiviById(Long id);

    List<Suivi> getAllSuivis();

    Page<Suivi> getAllSuivisPaginated(Pageable pageable);

    List<Suivi> getSuivisByArbreId(Long arbreId);

    Optional<Suivi> getSuiviByArbreIdAndDate(Long arbreId, java.time.LocalDate dateSuivi);

    List<Suivi> getSuivisByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate);

    List<Suivi> getSuivisByEtatSante(EtatSante etatSante);
}
