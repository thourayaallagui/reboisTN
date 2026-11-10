package ms_inscriptions.service;

import ms_inscriptions.dto.InscriptionRequest;
import ms_inscriptions.dto.InscriptionResponse;
import ms_inscriptions.entity.Inscription;
import ms_inscriptions.entity.StatutInscription;
import ms_inscriptions.exception.InscriptionDuplicateException;
import ms_inscriptions.exception.InscriptionNotFoundException;
import ms_inscriptions.exception.PresenceInvalideException;
import ms_inscriptions.exception.TransitionStatutInvalideException;
import ms_inscriptions.repository.InscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class InscriptionServiceImpl implements InscriptionService {

	private final InscriptionRepository inscriptionRepository;

	public InscriptionServiceImpl(InscriptionRepository inscriptionRepository) {
		this.inscriptionRepository = inscriptionRepository;
	}

	@Override
	@Transactional
	public InscriptionResponse creer(InscriptionRequest request) {
		verifierAbsenceDoublon(request.identifiantBenevole(), request.identifiantEvenement());
		Inscription inscription = new Inscription(
				request.identifiantBenevole(), request.identifiantEvenement());
		return versResponse(inscriptionRepository.save(inscription));
	}

	@Override
	@Transactional(readOnly = true)
	public List<InscriptionResponse> trouverTous() {
		return inscriptionRepository.findAll().stream().map(this::versResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public InscriptionResponse trouverParId(Long id) {
		return versResponse(trouverEntite(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<InscriptionResponse> trouverParBenevole(Long identifiantBenevole) {
		return inscriptionRepository.findByIdentifiantBenevole(identifiantBenevole)
				.stream().map(this::versResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<InscriptionResponse> trouverParEvenement(Long identifiantEvenement) {
		return inscriptionRepository.findByIdentifiantEvenement(identifiantEvenement)
				.stream().map(this::versResponse).toList();
	}

	@Override
	@Transactional
	public InscriptionResponse modifier(Long id, InscriptionRequest request) {
		Inscription inscription = trouverEntite(id);
		if (inscription.getStatut() == StatutInscription.ANNULEE) {
			throw new TransitionStatutInvalideException(
					"Une inscription annulée ne peut pas être modifiée.");
		}

		boolean identifiantsChanges = !Objects.equals(
				inscription.getIdentifiantBenevole(), request.identifiantBenevole())
				|| !Objects.equals(
						inscription.getIdentifiantEvenement(), request.identifiantEvenement());
		if (identifiantsChanges) {
			verifierAbsenceDoublon(request.identifiantBenevole(), request.identifiantEvenement());
		}

		inscription.setIdentifiantBenevole(request.identifiantBenevole());
		inscription.setIdentifiantEvenement(request.identifiantEvenement());
		return versResponse(inscriptionRepository.save(inscription));
	}

	@Override
	@Transactional
	public InscriptionResponse confirmer(Long id) {
		Inscription inscription = trouverEntite(id);
		if (inscription.getStatut() != StatutInscription.EN_ATTENTE) {
			throw new TransitionStatutInvalideException(
					"Seule une inscription en attente peut être confirmée.");
		}

		inscription.setStatut(StatutInscription.CONFIRMEE);
		return versResponse(inscriptionRepository.save(inscription));
	}

	@Override
	@Transactional
	public InscriptionResponse annuler(Long id) {
		Inscription inscription = trouverEntite(id);
		if (inscription.getStatut() != StatutInscription.EN_ATTENTE
				&& inscription.getStatut() != StatutInscription.CONFIRMEE) {
			throw new TransitionStatutInvalideException(
					"Seule une inscription en attente ou confirmée peut être annulée.");
		}

		inscription.setStatut(StatutInscription.ANNULEE);
		return versResponse(inscriptionRepository.save(inscription));
	}

	@Override
	@Transactional
	public InscriptionResponse enregistrerPresence(Long id) {
		Inscription inscription = trouverEntite(id);
		if (inscription.getStatut() != StatutInscription.CONFIRMEE) {
			throw new PresenceInvalideException(id);
		}

		inscription.setPresence(true);
		return versResponse(inscriptionRepository.save(inscription));
	}

	@Override
	@Transactional
	public void supprimer(Long id) {
		inscriptionRepository.delete(trouverEntite(id));
	}

	private Inscription trouverEntite(Long id) {
		return inscriptionRepository.findById(id)
				.orElseThrow(() -> new InscriptionNotFoundException(id));
	}

	private void verifierAbsenceDoublon(Long identifiantBenevole, Long identifiantEvenement) {
		if (inscriptionRepository.existsByIdentifiantBenevoleAndIdentifiantEvenement(
				identifiantBenevole, identifiantEvenement)) {
			throw new InscriptionDuplicateException(identifiantBenevole, identifiantEvenement);
		}
	}

	private InscriptionResponse versResponse(Inscription inscription) {
		return new InscriptionResponse(
				inscription.getId(),
				inscription.getDateInscription(),
				inscription.getStatut(),
				inscription.getIdentifiantBenevole(),
				inscription.getIdentifiantEvenement(),
				inscription.isPresence());
	}
}
