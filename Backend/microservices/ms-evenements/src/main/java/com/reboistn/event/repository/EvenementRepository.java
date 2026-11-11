package com.reboistn.event.repository;

import com.reboistn.event.entity.Evenement;
import com.reboistn.event.enums.StatutEvenement;
import com.reboistn.event.enums.TypeEvenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    List<Evenement> findByVilleIgnoreCase(String ville);

    List<Evenement> findByTypeEvenement(TypeEvenement typeEvenement);

    List<Evenement> findByStatut(StatutEvenement statut);

    List<Evenement> findByOrganisateurId(Long organisateurId);

    List<Evenement> findByTypeEvenementAndStatut(TypeEvenement typeEvenement, StatutEvenement statut);

    List<Evenement> findByVilleIgnoreCaseAndStatut(String ville, StatutEvenement statut);
}
