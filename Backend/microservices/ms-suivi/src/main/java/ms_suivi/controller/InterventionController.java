package ms_suivi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ms_suivi.entity.Intervention;
import ms_suivi.service.InterventionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/api/interventions", "/api/suivis/interventions"})
@RequiredArgsConstructor
public class InterventionController {

    private final InterventionService interventionService;

    @PostMapping
    public ResponseEntity<Intervention> createIntervention(@Valid @RequestBody Intervention intervention) {
        Intervention createdIntervention = interventionService.createIntervention(intervention);
        return new ResponseEntity<>(createdIntervention, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Intervention> updateIntervention(
            @PathVariable Long id,
            @Valid @RequestBody Intervention intervention) {
        Intervention updatedIntervention = interventionService.updateIntervention(id, intervention);
        return ResponseEntity.ok(updatedIntervention);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntervention(@PathVariable Long id) {
        interventionService.deleteIntervention(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Intervention> getInterventionById(@PathVariable Long id) {
        return ResponseEntity.ok(interventionService.getInterventionById(id));
    }

    @GetMapping
    public ResponseEntity<List<Intervention>> getAllInterventions() {
        List<Intervention> interventions = interventionService.getAllInterventions();
        return ResponseEntity.ok(interventions);
    }

    @GetMapping("/suivi/{suiviId}")
    public ResponseEntity<List<Intervention>> getInterventionsBySuiviId(@PathVariable Long suiviId) {
        List<Intervention> interventions = interventionService.getInterventionsBySuiviId(suiviId);
        return ResponseEntity.ok(interventions);
    }

    @GetMapping("/type/{typeIntervention}")
    public ResponseEntity<List<Intervention>> getInterventionsByType(@PathVariable String typeIntervention) {
        List<Intervention> interventions = interventionService.getInterventionsByType(typeIntervention);
        return ResponseEntity.ok(interventions);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Intervention>> getInterventionsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<Intervention> interventions = interventionService.getInterventionsByDateRange(start, end);
        return ResponseEntity.ok(interventions);
    }
}
