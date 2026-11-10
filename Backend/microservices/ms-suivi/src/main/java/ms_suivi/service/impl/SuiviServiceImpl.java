package ms_suivi.service.impl;

import lombok.RequiredArgsConstructor;
import ms_suivi.entity.EtatSante;
import ms_suivi.entity.Suivi;
import ms_suivi.exception.SuiviNotFoundException;
import ms_suivi.repository.SuiviRepository;
import ms_suivi.service.SuiviService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SuiviServiceImpl implements SuiviService {

    private final SuiviRepository suiviRepository;

    @Override
    public Suivi createSuivi(Suivi suivi) {
        return suiviRepository.save(suivi);
    }

    @Override
    public Suivi updateSuivi(Long id, Suivi suivi) {
        Suivi existingSuivi = suiviRepository.findById(id)
                .orElseThrow(() -> new SuiviNotFoundException(id));

        existingSuivi.setArbreId(suivi.getArbreId());
        existingSuivi.setDateSuivi(suivi.getDateSuivi());
        existingSuivi.setHauteur(suivi.getHauteur());
        existingSuivi.setDiametreTronc(suivi.getDiametreTronc());
        existingSuivi.setEtatSante(suivi.getEtatSante());
        existingSuivi.setObservation(suivi.getObservation());

        return suiviRepository.save(existingSuivi);
    }

    @Override
    public void deleteSuivi(Long id) {
        if (!suiviRepository.existsById(id)) {
            throw new SuiviNotFoundException(id);
        }
        suiviRepository.deleteById(id);
    }

    @Override
    public Optional<Suivi> getSuiviById(Long id) {
        return suiviRepository.findById(id);
    }

    @Override
    public List<Suivi> getAllSuivis() {
        return suiviRepository.findAll();
    }

    @Override
    public Page<Suivi> getAllSuivisPaginated(Pageable pageable) {
        return suiviRepository.findAll(pageable);
    }

    @Override
    public List<Suivi> getSuivisByArbreId(Long arbreId) {
        return suiviRepository.findByArbreId(arbreId);
    }

    @Override
    public Optional<Suivi> getSuiviByArbreIdAndDate(Long arbreId, java.time.LocalDate dateSuivi) {
        return suiviRepository.findByArbreIdAndDateSuivi(arbreId, dateSuivi);
    }

    @Override
    public List<Suivi> getSuivisByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        return suiviRepository.findByDateSuiviBetween(startDate, endDate);
    }

    @Override
    public List<Suivi> getSuivisByEtatSante(EtatSante etatSante) {
        return suiviRepository.findByEtatSante(etatSante);
    }
}
