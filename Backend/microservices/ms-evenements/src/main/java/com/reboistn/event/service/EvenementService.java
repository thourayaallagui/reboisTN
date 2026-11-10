package com.reboistn.event.service;

import com.reboistn.event.dto.EvenementRequest;
import com.reboistn.event.dto.EvenementResponse;
import com.reboistn.event.entity.Evenement;
import com.reboistn.event.enums.StatutEvenement;
import com.reboistn.event.enums.TypeEvenement;
import com.reboistn.event.exception.BusinessException;
import com.reboistn.event.exception.ResourceNotFoundException;
import com.reboistn.event.repository.EvenementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvenementService {

    private final EvenementRepository evenementRepository;

    public EvenementService(EvenementRepository evenementRepository) {
        this.evenementRepository = evenementRepository;
    }

    @Transactional
    public EvenementResponse createEvenement(EvenementRequest request) {
        validateDatesAndCapacity(request.getDateDebut(), request.getDateFin(), request.getNombrePlaces());

        Evenement evenement = new Evenement();
        evenement.setTitre(request.getTitre());
        evenement.setDescription(request.getDescription());
        evenement.setTypeEvenement(request.getTypeEvenement());
        evenement.setDateDebut(request.getDateDebut());
        evenement.setDateFin(request.getDateFin());
        evenement.setLieu(request.getLieu());
        evenement.setVille(request.getVille());
        evenement.setLatitude(request.getLatitude());
        evenement.setLongitude(request.getLongitude());
        evenement.setNombrePlaces(request.getNombrePlaces());
        // Règle 8 & 9 : statut initial BROUILLON, nombre d'inscrits initial 0
        evenement.setStatut(StatutEvenement.BROUILLON);
        evenement.setNombreInscrits(0);
        evenement.setImageUrl(request.getImageUrl());
        evenement.setOrganisateurId(request.getOrganisateurId());

        Evenement saved = evenementRepository.save(evenement);
        return EvenementResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<EvenementResponse> getAllEvenements(TypeEvenement type, StatutEvenement statut, String ville) {
        List<Evenement> list;
        if (type != null && statut != null) {
            list = evenementRepository.findByTypeEvenementAndStatut(type, statut);
        } else if (type != null) {
            list = evenementRepository.findByTypeEvenement(type);
        } else if (statut != null) {
            list = evenementRepository.findByStatut(statut);
        } else if (ville != null && !ville.isBlank()) {
            list = evenementRepository.findByVilleIgnoreCase(ville);
        } else {
            list = evenementRepository.findAll();
        }
        return list.stream()
                .map(EvenementResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EvenementResponse getEvenementById(Long id) {
        Evenement evenement = findByIdOrThrow(id);
        return EvenementResponse.fromEntity(evenement);
    }

    @Transactional
    public EvenementResponse updateEvenement(Long id, EvenementRequest request) {
        Evenement existing = findByIdOrThrow(id);
        validateDatesAndCapacity(request.getDateDebut(), request.getDateFin(), request.getNombrePlaces());

        // Règle 5 : le nombre d'inscrits existant ne peut pas dépasser la nouvelle capacité
        if (existing.getNombreInscrits() > request.getNombrePlaces()) {
            throw new BusinessException("La nouvelle capacité (" + request.getNombrePlaces() +
                    ") ne peut pas être inférieure au nombre d'inscrits actuels (" + existing.getNombreInscrits() + ")");
        }

        existing.setTitre(request.getTitre());
        existing.setDescription(request.getDescription());
        existing.setTypeEvenement(request.getTypeEvenement());
        existing.setDateDebut(request.getDateDebut());
        existing.setDateFin(request.getDateFin());
        existing.setLieu(request.getLieu());
        existing.setVille(request.getVille());
        existing.setLatitude(request.getLatitude());
        existing.setLongitude(request.getLongitude());
        existing.setNombrePlaces(request.getNombrePlaces());
        existing.setImageUrl(request.getImageUrl());
        existing.setOrganisateurId(request.getOrganisateurId());

        Evenement updated = evenementRepository.save(existing);
        return EvenementResponse.fromEntity(updated);
    }

    @Transactional
    public void deleteEvenement(Long id) {
        Evenement evenement = findByIdOrThrow(id);
        evenementRepository.delete(evenement);
    }

    @Transactional
    public EvenementResponse updateStatut(Long id, StatutEvenement nouveauStatut) {
        Evenement evenement = findByIdOrThrow(id);

        // Règle 6 : Un événement annulé ne peut plus être publié
        if (evenement.getStatut() == StatutEvenement.ANNULE && nouveauStatut == StatutEvenement.PUBLIE) {
            throw new BusinessException("Un événement annulé ne peut plus être publié");
        }

        evenement.setStatut(nouveauStatut);
        Evenement saved = evenementRepository.save(evenement);
        return EvenementResponse.fromEntity(saved);
    }

    @Transactional
    public EvenementResponse inscrire(Long id) {
        Evenement evenement = findByIdOrThrow(id);

        // Règle 7 : Un événement terminé ne peut plus accepter de nouvelles inscriptions
        if (evenement.getStatut() == StatutEvenement.TERMINE) {
            throw new BusinessException("Un événement terminé ne peut plus accepter de nouvelles inscriptions");
        }
        if (evenement.getStatut() == StatutEvenement.ANNULE) {
            throw new BusinessException("Impossible de s'inscrire à un événement annulé");
        }
        if (evenement.getStatut() == StatutEvenement.BROUILLON) {
            throw new BusinessException("Impossible de s'inscrire à un événement encore en brouillon");
        }

        // Règle 5 : Le nombre d'inscrits ne doit pas dépasser le nombre de places
        if (evenement.getNombreInscrits() >= evenement.getNombrePlaces()) {
            throw new BusinessException("Événement complet : aucune place disponible");
        }

        evenement.setNombreInscrits(evenement.getNombreInscrits() + 1);
        Evenement saved = evenementRepository.save(evenement);
        return EvenementResponse.fromEntity(saved);
    }

    @Transactional
    public EvenementResponse desinscrire(Long id) {
        Evenement evenement = findByIdOrThrow(id);

        // Règle 4 : Le nombre d'inscrits ne doit jamais être négatif
        if (evenement.getNombreInscrits() <= 0) {
            throw new BusinessException("Aucun inscrit à désinscrire pour cet événement");
        }

        evenement.setNombreInscrits(evenement.getNombreInscrits() - 1);
        Evenement saved = evenementRepository.save(evenement);
        return EvenementResponse.fromEntity(saved);
    }

    private Evenement findByIdOrThrow(Long id) {
        return evenementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé avec l'ID : " + id));
    }

    private void validateDatesAndCapacity(java.time.LocalDateTime dateDebut, java.time.LocalDateTime dateFin, Integer nombrePlaces) {
        // Règle 2 : La date de fin doit être postérieure à la date de début
        if (dateDebut == null || dateFin == null || !dateFin.isAfter(dateDebut)) {
            throw new BusinessException("La date de fin doit être strictement postérieure à la date de début");
        }
        // Règle 3 : Le nombre de places doit être strictement positif
        if (nombrePlaces == null || nombrePlaces <= 0) {
            throw new BusinessException("Le nombre de places doit être strictement positif");
        }
    }
}
