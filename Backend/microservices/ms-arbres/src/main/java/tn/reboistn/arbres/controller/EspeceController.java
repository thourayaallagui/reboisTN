package tn.reboistn.arbres.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.reboistn.arbres.model.Espece;
import tn.reboistn.arbres.repository.ArbreRepository;
import tn.reboistn.arbres.repository.EspeceRepository;

import java.util.List;

@RestController
@RequestMapping("/api/arbres/especes")
public class EspeceController {

    private final EspeceRepository repository;
    private final ArbreRepository arbreRepository;

    public EspeceController(EspeceRepository repository, ArbreRepository arbreRepository) {
        this.repository = repository;
        this.arbreRepository = arbreRepository;
    }

    @GetMapping
    public List<Espece> lister() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Espece detail(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Espece introuvable"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Espece creer(@Valid @RequestBody Espece espece) {
        if (repository.existsByNomCommun(espece.getNomCommun())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Espece deja existante");
        }
        espece.setId(null);
        return repository.save(espece);
    }

    @PutMapping("/{id}")
    public Espece modifier(@PathVariable Long id, @Valid @RequestBody Espece donnees) {
        Espece existante = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Espece introuvable"));
        // on refuse si le nouveau nom est deja pris par une autre espece
        if (!existante.getNomCommun().equals(donnees.getNomCommun())
                && repository.existsByNomCommun(donnees.getNomCommun())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Espece deja existante");
        }
        existante.setNomCommun(donnees.getNomCommun());
        existante.setNomScientifique(donnees.getNomScientifique());
        return repository.save(existante);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Espece introuvable");
        }
        // une espece encore utilisee par des arbres ne peut pas etre supprimee
        if (arbreRepository.existsByEspeceId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Espece utilisee par des arbres");
        }
        repository.deleteById(id);
    }
}
