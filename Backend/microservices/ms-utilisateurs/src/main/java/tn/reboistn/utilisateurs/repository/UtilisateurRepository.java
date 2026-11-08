package tn.reboistn.utilisateurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.reboistn.utilisateurs.model.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByKeycloakId(String keycloakId);
    boolean existsByKeycloakId(String keycloakId);
    boolean existsByEmail(String email);
}
