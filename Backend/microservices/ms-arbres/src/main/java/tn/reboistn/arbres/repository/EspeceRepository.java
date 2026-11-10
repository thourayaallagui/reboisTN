package tn.reboistn.arbres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.reboistn.arbres.model.Espece;

public interface EspeceRepository extends JpaRepository<Espece, Long> {
    boolean existsByNomCommun(String nomCommun);
}
