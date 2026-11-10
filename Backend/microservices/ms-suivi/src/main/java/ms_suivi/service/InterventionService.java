package ms_suivi.service;

import ms_suivi.entity.Intervention;

import java.util.List;

public interface InterventionService {

    Intervention createIntervention(Intervention intervention);

    Intervention updateIntervention(Long id, Intervention intervention);

    void deleteIntervention(Long id);

    Intervention getInterventionById(Long id);

    List<Intervention> getAllInterventions();

    List<Intervention> getInterventionsBySuiviId(Long suiviId);

    List<Intervention> getInterventionsByType(String typeIntervention);

    List<Intervention> getInterventionsByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate);
}
