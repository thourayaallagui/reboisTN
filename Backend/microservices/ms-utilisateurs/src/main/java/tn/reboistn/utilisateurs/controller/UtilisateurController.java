package tn.reboistn.utilisateurs.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.reboistn.utilisateurs.model.Utilisateur;
import tn.reboistn.utilisateurs.repository.UtilisateurRepository;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurRepository repository;

    public UtilisateurController(UtilisateurRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Utilisateur> lister() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Utilisateur detail(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
    }

    /**
     * Profil de l utilisateur connecté.
     * Provisoire : l identifiant Keycloak vient d un en-tête ; il sera lu dans le JWT en S8.
     */
    @GetMapping("/me")
    public Utilisateur moi(@RequestHeader(value = "X-Keycloak-Id", required = false) String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié");
        }
        return repository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profil introuvable"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utilisateur creer(@Valid @RequestBody Utilisateur utilisateur) {
        if (repository.existsByKeycloakId(utilisateur.getKeycloakId())
                || repository.existsByEmail(utilisateur.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Utilisateur déjà existant");
        }
        utilisateur.setId(null);
        return repository.save(utilisateur);
    }

    @PutMapping("/{id}")
    public Utilisateur modifier(@PathVariable Long id, @Valid @RequestBody Utilisateur donnees) {
        Utilisateur existant = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
        existant.setNom(donnees.getNom());
        existant.setPrenom(donnees.getPrenom());
        existant.setEmail(donnees.getEmail());
        existant.setTelephone(donnees.getTelephone());
        existant.setRole(donnees.getRole());
        existant.setOrganisationId(donnees.getOrganisationId());
        return repository.save(existant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable");
        }
        repository.deleteById(id);
    }
}
