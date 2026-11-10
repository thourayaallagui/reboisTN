package ms_inscriptions.repository;

import ms_inscriptions.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Accès aux inscriptions persistées. */
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

	List<Inscription> findByIdentifiantBenevole(Long identifiantBenevole);

	List<Inscription> findByIdentifiantEvenement(Long identifiantEvenement);

	boolean existsByIdentifiantBenevoleAndIdentifiantEvenement(
			Long identifiantBenevole,
			Long identifiantEvenement
	);
}
