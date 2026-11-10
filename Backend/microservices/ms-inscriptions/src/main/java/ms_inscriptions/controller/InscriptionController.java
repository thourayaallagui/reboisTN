package ms_inscriptions.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ms_inscriptions.dto.InscriptionRequest;
import ms_inscriptions.dto.InscriptionResponse;
import ms_inscriptions.service.InscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Expose l'API REST de gestion des inscriptions. */
@RestController
@RequestMapping("/api/inscriptions")
@Tag(name = "Inscriptions", description = "Gestion des inscriptions aux événements ReboisTN")
public class InscriptionController {

	private final InscriptionService inscriptionService;

	public InscriptionController(InscriptionService inscriptionService) {
		this.inscriptionService = inscriptionService;
	}

	@PostMapping
	@Operation(summary = "Créer une inscription")
	public ResponseEntity<InscriptionResponse> creer(@Valid @RequestBody InscriptionRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(inscriptionService.creer(request));
	}

	@GetMapping
	@Operation(summary = "Lister toutes les inscriptions")
	public List<InscriptionResponse> trouverTous() {
		return inscriptionService.trouverTous();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Consulter une inscription")
	public InscriptionResponse trouverParId(@PathVariable Long id) {
		return inscriptionService.trouverParId(id);
	}

	@GetMapping("/benevole/{identifiantBenevole}")
	@Operation(summary = "Lister les inscriptions d'un bénévole")
	public List<InscriptionResponse> trouverParBenevole(@PathVariable Long identifiantBenevole) {
		return inscriptionService.trouverParBenevole(identifiantBenevole);
	}

	@GetMapping("/evenement/{identifiantEvenement}")
	@Operation(summary = "Lister les inscriptions d'un événement")
	public List<InscriptionResponse> trouverParEvenement(@PathVariable Long identifiantEvenement) {
		return inscriptionService.trouverParEvenement(identifiantEvenement);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Modifier une inscription")
	public InscriptionResponse modifier(
			@PathVariable Long id,
			@Valid @RequestBody InscriptionRequest request) {
		return inscriptionService.modifier(id, request);
	}

	@PatchMapping("/{id}/confirmer")
	@Operation(summary = "Confirmer une inscription")
	public InscriptionResponse confirmer(@PathVariable Long id) {
		return inscriptionService.confirmer(id);
	}

	@PatchMapping("/{id}/annuler")
	@Operation(summary = "Annuler une inscription")
	public InscriptionResponse annuler(@PathVariable Long id) {
		return inscriptionService.annuler(id);
	}

	@PatchMapping("/{id}/presence")
	@Operation(summary = "Enregistrer la présence")
	public InscriptionResponse enregistrerPresence(@PathVariable Long id) {
		return inscriptionService.enregistrerPresence(id);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Supprimer une inscription")
	public ResponseEntity<Void> supprimer(@PathVariable Long id) {
		inscriptionService.supprimer(id);
		return ResponseEntity.noContent().build();
	}
}
