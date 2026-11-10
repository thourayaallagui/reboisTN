package ms_inscriptions.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscriptions")
public class Inscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "date_inscription", nullable = false, updatable = false)
	private LocalDateTime dateInscription;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StatutInscription statut = StatutInscription.EN_ATTENTE;

	@NotNull
	@Column(name = "identifiant_benevole", nullable = false)
	private Long identifiantBenevole;

	@NotNull
	@Column(name = "identifiant_evenement", nullable = false)
	private Long identifiantEvenement;

	@Column(nullable = false)
	private boolean presence = false;

	protected Inscription() {
		// Constructeur requis par JPA.
	}

	public Inscription(Long identifiantBenevole, Long identifiantEvenement) {
		this.identifiantBenevole = identifiantBenevole;
		this.identifiantEvenement = identifiantEvenement;
	}

	@PrePersist
	void initialiserDateInscription() {
		if (dateInscription == null) {
			dateInscription = LocalDateTime.now();
		}
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getDateInscription() {
		return dateInscription;
	}

	public StatutInscription getStatut() {
		return statut;
	}

	public void setStatut(StatutInscription statut) {
		this.statut = statut;
	}

	public Long getIdentifiantBenevole() {
		return identifiantBenevole;
	}

	public void setIdentifiantBenevole(Long identifiantBenevole) {
		this.identifiantBenevole = identifiantBenevole;
	}

	public Long getIdentifiantEvenement() {
		return identifiantEvenement;
	}

	public void setIdentifiantEvenement(Long identifiantEvenement) {
		this.identifiantEvenement = identifiantEvenement;
	}

	public boolean isPresence() {
		return presence;
	}

	public void setPresence(boolean presence) {
		this.presence = presence;
	}
}
