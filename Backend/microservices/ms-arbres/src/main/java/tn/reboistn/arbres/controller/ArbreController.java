package tn.reboistn.arbres.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.reboistn.arbres.model.Arbre;
import tn.reboistn.arbres.model.Espece;
import tn.reboistn.arbres.model.StatutArbre;
import tn.reboistn.arbres.repository.ArbreRepository;
import tn.reboistn.arbres.repository.EspeceRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/arbres")
public class ArbreController {

    private final ArbreRepository repository;
    private final EspeceRepository especeRepository;

    public ArbreController(ArbreRepository repository, EspeceRepository especeRepository) {
        this.repository = repository;
        this.especeRepository = especeRepository;
    }

    /** Test rapide : verifier que le service tourne (en direct ou via la gateway). */
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("service", "ms-arbres", "message", "Hello from ms-arbres");
    }

    @GetMapping
    public List<Arbre> lister(@RequestParam(required = false) String gouvernorat,
                              @RequestParam(required = false) Long evenementId) {
        if (gouvernorat != null) {
            return repository.findByGouvernorat(gouvernorat);
        }
        if (evenementId != null) {
            return repository.findByEvenementId(evenementId);
        }
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Arbre detail(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Arbre introuvable"));
    }

    // TODO S6 : verifier l evenement avec OpenFeign (ms-evenements) avant d enregistrer
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Arbre creer(@Valid @RequestBody Arbre arbre) {
        Long especeId = arbre.getEspece().getId();
        Espece espece = especeRepository.findById(especeId == null ? -1L : especeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Espece introuvable"));
        arbre.setId(null);
        arbre.setEspece(espece);
        arbre.setStatut(StatutArbre.PLANTE);
        return repository.save(arbre);
    }

    @PutMapping("/{id}")
    public Arbre modifier(@PathVariable Long id, @Valid @RequestBody Arbre donnees) {
        Arbre existant = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Arbre introuvable"));
        Long especeId = donnees.getEspece().getId();
        Espece espece = especeRepository.findById(especeId == null ? -1L : especeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Espece introuvable"));
        existant.setEvenementId(donnees.getEvenementId());
        existant.setPlanteParId(donnees.getPlanteParId());
        existant.setDatePlantation(donnees.getDatePlantation());
        existant.setLatitude(donnees.getLatitude());
        existant.setLongitude(donnees.getLongitude());
        existant.setGouvernorat(donnees.getGouvernorat());
        existant.setEspece(espece);
        // si le statut n est pas envoye on garde l ancien
        if (donnees.getStatut() != null) {
            existant.setStatut(donnees.getStatut());
        }
        return repository.save(existant);
    }

    @PatchMapping("/{id}/statut")
    public Arbre changerStatut(@PathVariable Long id, @RequestParam StatutArbre statut) {
        Arbre arbre = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Arbre introuvable"));
        arbre.setStatut(statut);
        return repository.save(arbre);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arbre introuvable");
        }
        repository.deleteById(id);
    }
}
