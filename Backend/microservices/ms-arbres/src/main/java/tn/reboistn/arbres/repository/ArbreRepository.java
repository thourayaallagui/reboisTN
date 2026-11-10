package tn.reboistn.arbres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.reboistn.arbres.model.Arbre;

import java.util.List;

public interface ArbreRepository extends JpaRepository<Arbre, Long> {
    List<Arbre> findByGouvernorat(String gouvernorat);
    List<Arbre> findByEvenementId(Long evenementId);
    boolean existsByEspeceId(Long especeId);
}
