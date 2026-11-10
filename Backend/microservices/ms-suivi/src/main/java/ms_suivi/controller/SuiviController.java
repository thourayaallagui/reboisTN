package ms_suivi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ms_suivi.entity.EtatSante;
import ms_suivi.entity.Suivi;
import ms_suivi.exception.SuiviNotFoundException;
import ms_suivi.service.SuiviService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/suivis")
@RequiredArgsConstructor
@Tag(name = "Suivi Management", description = "APIs for managing tree tracking records")
public class SuiviController {

    private final SuiviService suiviService;

    @PostMapping
    @Operation(summary = "Create a new suivi", description = "Create a new tree tracking record")
    public ResponseEntity<Suivi> createSuivi(@Valid @RequestBody Suivi suivi) {
        Suivi createdSuivi = suiviService.createSuivi(suivi);
        return new ResponseEntity<>(createdSuivi, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a suivi", description = "Update an existing tree tracking record")
    public ResponseEntity<Suivi> updateSuivi(
            @Parameter(description = "Suivi ID") @PathVariable Long id,
            @Valid @RequestBody Suivi suivi) {
        Suivi updatedSuivi = suiviService.updateSuivi(id, suivi);
        return ResponseEntity.ok(updatedSuivi);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a suivi", description = "Delete a tree tracking record by ID")
    public ResponseEntity<Void> deleteSuivi(@Parameter(description = "Suivi ID") @PathVariable Long id) {
        suiviService.deleteSuivi(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a suivi by ID", description = "Retrieve a tree tracking record by its ID")
    public ResponseEntity<Suivi> getSuiviById(@Parameter(description = "Suivi ID") @PathVariable Long id) {
        return suiviService.getSuiviById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new SuiviNotFoundException(id));
    }

    @GetMapping
    @Operation(summary = "Get all suivis", description = "Retrieve all tree tracking records")
    public ResponseEntity<List<Suivi>> getAllSuivis() {
        List<Suivi> suivis = suiviService.getAllSuivis();
        return ResponseEntity.ok(suivis);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get all suivis with pagination", description = "Retrieve tree tracking records with pagination and sorting")
    public ResponseEntity<Page<Suivi>> getAllSuivisPaginated(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "dateSuivi") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(defaultValue = "DESC") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Suivi> suivis = suiviService.getAllSuivisPaginated(pageable);
        return ResponseEntity.ok(suivis);
    }

    @GetMapping("/arbre/{arbreId}")
    @Operation(summary = "Get suivis by tree ID", description = "Retrieve all tracking records for a specific tree")
    public ResponseEntity<List<Suivi>> getSuivisByArbreId(
            @Parameter(description = "Tree ID") @PathVariable Long arbreId) {
        List<Suivi> suivis = suiviService.getSuivisByArbreId(arbreId);
        return ResponseEntity.ok(suivis);
    }

    @GetMapping("/arbre/{arbreId}/date/{dateSuivi}")
    @Operation(summary = "Get suivi by tree ID and date", description = "Retrieve a tracking record for a specific tree on a specific date")
    public ResponseEntity<Suivi> getSuiviByArbreIdAndDate(
            @Parameter(description = "Tree ID") @PathVariable Long arbreId,
            @Parameter(description = "Tracking date (yyyy-MM-dd)") @PathVariable String dateSuivi) {
        LocalDate date = LocalDate.parse(dateSuivi);
        return suiviService.getSuiviByArbreIdAndDate(arbreId, date)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get suivis by date range", description = "Retrieve tracking records within a date range")
    public ResponseEntity<List<Suivi>> getSuivisByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<Suivi> suivis = suiviService.getSuivisByDateRange(start, end);
        return ResponseEntity.ok(suivis);
    }

    @GetMapping("/etat/{etatSante}")
    @Operation(summary = "Get suivis by health status", description = "Retrieve tracking records by health status")
    public ResponseEntity<List<Suivi>> getSuivisByEtatSante(
            @Parameter(description = "Health status") @PathVariable EtatSante etatSante) {
        List<Suivi> suivis = suiviService.getSuivisByEtatSante(etatSante);
        return ResponseEntity.ok(suivis);
    }
}
