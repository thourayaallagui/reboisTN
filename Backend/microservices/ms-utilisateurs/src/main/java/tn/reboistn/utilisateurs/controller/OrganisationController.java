package tn.reboistn.utilisateurs.controller;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.reboistn.utilisateurs.model.Organisation;
import tn.reboistn.utilisateurs.repository.OrganisationRepository;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs/organisations")
public class OrganisationController {

    private final OrganisationRepository repository;

    public OrganisationController(OrganisationRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Organisation> lister() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Organisation detail(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Organisation introuvable"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Organisation creer(@Valid @RequestBody Organisation organisation) {
        organisation.setId(null);
        return repository.save(organisation);
    }
}
