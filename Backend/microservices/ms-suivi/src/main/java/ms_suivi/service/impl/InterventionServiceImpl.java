package ms_suivi.service.impl;

import lombok.RequiredArgsConstructor;
import ms_suivi.entity.Intervention;
import ms_suivi.repository.InterventionRepository;
import ms_suivi.service.InterventionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InterventionServiceImpl implements InterventionService {

    private final InterventionRepository interventionRepository;

    @Override
    public Intervention createIntervention(Intervention intervention) {
        return interventionRepository.save(intervention);
    }

    @Override
    public Intervention updateIntervention(Long id, Intervention intervention) {
        Intervention existingIntervention = interventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intervention not found with id: " + id));

        existingIntervention.setSuivi(intervention.getSuivi());
        existingIntervention.setTypeIntervention(intervention.getTypeIntervention());
        existingIntervention.setDateIntervention(intervention.getDateIntervention());
        existingIntervention.setDescription(intervention.getDescription());

        return interventionRepository.save(existingIntervention);
    }

    @Override
    public void deleteIntervention(Long id) {
        if (!interventionRepository.existsById(id)) {
            throw new RuntimeException("Intervention not found with id: " + id);
        }
        interventionRepository.deleteById(id);
    }

    @Override
    public Intervention getInterventionById(Long id) {
        return interventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intervention not found with id: " + id));
    }

    @Override
    public List<Intervention> getAllInterventions() {
        return interventionRepository.findAll();
    }

    @Override
    public List<Intervention> getInterventionsBySuiviId(Long suiviId) {
        return interventionRepository.findBySuiviId(suiviId);
    }

    @Override
    public List<Intervention> getInterventionsByType(String typeIntervention) {
        return interventionRepository.findByTypeIntervention(typeIntervention);
    }

    @Override
    public List<Intervention> getInterventionsByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        return interventionRepository.findByDateInterventionBetween(startDate, endDate);
    }
}
