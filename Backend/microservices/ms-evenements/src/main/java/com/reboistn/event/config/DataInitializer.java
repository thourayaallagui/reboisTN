package com.reboistn.event.config;

import com.reboistn.event.entity.Evenement;
import com.reboistn.event.enums.StatutEvenement;
import com.reboistn.event.enums.TypeEvenement;
import com.reboistn.event.repository.EvenementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initDatabase(EvenementRepository evenementRepository) {
        return args -> {
            if (evenementRepository.count() > 0) {
                log.info("La table des événements contient déjà des données. Initialisation ignorée.");
                return;
            }

            log.info("Initialisation des événements de démonstration pour la Tunisie...");

            // 1. Plantation d'arbres à Tunis
            Evenement plantationTunis = new Evenement(
                    null,
                    "Grande plantation citoyenne au Belvédère",
                    "Opération majeure de reboisement urbain et re-végétalisation du parc du Belvédère à Tunis avec plantation d'arbres méditerranéens (pins d'Alep, caroubiers).",
                    TypeEvenement.PLANTATION,
                    LocalDateTime.now().plusDays(7).withHour(9).withMinute(0).withSecond(0),
                    LocalDateTime.now().plusDays(7).withHour(14).withMinute(0).withSecond(0),
                    "Parc du Belvédère",
                    "Tunis",
                    36.8233,
                    10.1714,
                    100,
                    25,
                    "https://images.unsplash.com/photo-1542601906990-b4d3fb778b09",
                    StatutEvenement.PUBLIE,
                    1L
            );

            // 2. Nettoyage d'une plage à Bizerte
            Evenement nettoyageBizerte = new Evenement(
                    null,
                    "Nettoyage écocitoyen de la plage Sidi Salem",
                    "Collecte des déchets plastiques, mégots et résidus marins sur le littoral de Sidi Salem à Bizerte pour protéger la faune et la flore marine.",
                    TypeEvenement.NETTOYAGE,
                    LocalDateTime.now().plusDays(12).withHour(8).withMinute(30).withSecond(0),
                    LocalDateTime.now().plusDays(12).withHour(12).withMinute(30).withSecond(0),
                    "Plage Sidi Salem",
                    "Bizerte",
                    37.2842,
                    9.8739,
                    60,
                    18,
                    "https://images.unsplash.com/photo-1618477461853-cf6ed80faba5",
                    StatutEvenement.PUBLIE,
                    2L
            );

            // 3. Sensibilisation à l'environnement à Sousse
            Evenement sensibilisationSousse = new Evenement(
                    null,
                    "Atelier citoyen : Climat et biodiversité du Sahel",
                    "Journée éducative interactive ouverte aux familles et étudiants portant sur la lutte contre la désertification et les économies d'eau.",
                    TypeEvenement.SENSIBILISATION,
                    LocalDateTime.now().plusDays(15).withHour(10).withMinute(0).withSecond(0),
                    LocalDateTime.now().plusDays(15).withHour(16).withMinute(0).withSecond(0),
                    "Maison des Jeunes de Sousse",
                    "Sousse",
                    35.8256,
                    10.6369,
                    40,
                    12,
                    "https://images.unsplash.com/photo-1516937941344-00b4e0337589",
                    StatutEvenement.PUBLIE,
                    1L
            );

            // 4. Collecte et tri des déchets à Nabeul
            Evenement recyclageNabeul = new Evenement(
                    null,
                    "Opération éco-tri et recyclage du littoral de Nabeul",
                    "Campagne de collecte sélective des plastiques et métaux avec tri sur place et remise aux artisans et recycleurs locaux du Cap Bon.",
                    TypeEvenement.RECYCLAGE,
                    LocalDateTime.now().plusDays(20).withHour(9).withMinute(0).withSecond(0),
                    LocalDateTime.now().plusDays(20).withHour(13).withMinute(0).withSecond(0),
                    "Corniche de Nabeul (Face à la Jarre)",
                    "Nabeul",
                    36.4561,
                    10.7376,
                    50,
                    0,
                    "https://images.unsplash.com/photo-1532996122724-e3c354a0b15b",
                    StatutEvenement.BROUILLON,
                    3L
            );

            evenementRepository.saveAll(List.of(
                    plantationTunis,
                    nettoyageBizerte,
                    sensibilisationSousse,
                    recyclageNabeul
            ));

            log.info("4 événements de démonstration insérés avec succès dans la base H2 !");
        };
    }
}
