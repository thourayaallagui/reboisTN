package ms_inscriptions.service;

import ms_inscriptions.dto.InscriptionRequest;
import ms_inscriptions.dto.InscriptionResponse;

import java.util.List;

public interface InscriptionService {

	InscriptionResponse creer(InscriptionRequest request);

	List<InscriptionResponse> trouverTous();

	InscriptionResponse trouverParId(Long id);

	List<InscriptionResponse> trouverParBenevole(Long identifiantBenevole);

	List<InscriptionResponse> trouverParEvenement(Long identifiantEvenement);

	InscriptionResponse modifier(Long id, InscriptionRequest request);

	InscriptionResponse confirmer(Long id);

	InscriptionResponse annuler(Long id);

	InscriptionResponse enregistrerPresence(Long id);

	void supprimer(Long id);
}
