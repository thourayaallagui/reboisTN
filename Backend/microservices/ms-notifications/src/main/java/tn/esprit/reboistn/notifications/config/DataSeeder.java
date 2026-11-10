package tn.esprit.reboistn.notifications.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import tn.esprit.reboistn.notifications.entity.Notification;
import tn.esprit.reboistn.notifications.enums.CanalNotification;
import tn.esprit.reboistn.notifications.enums.TypeNotification;
import tn.esprit.reboistn.notifications.repository.NotificationRepository;

/**
 * Data seeder : insère 3 notifications de démonstration au démarrage (profil "dev" uniquement).
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    @Bean
    @Profile("dev")
    public CommandLineRunner seedNotifications(NotificationRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                log.info("Des notifications existent déjà, seeding ignoré.");
                return;
            }

            repository.save(Notification.builder()
                    .destinataireId("user-1")
                    .type(TypeNotification.EVENEMENT_CREE)
                    .canal(CanalNotification.IN_APP)
                    .titre("Nouvel événement de plantation")
                    .message("Un événement de plantation a été créé à Bizerte le 15/12/2026. Rejoignez-nous !")
                    .referenceId(101L)
                    .build());

            repository.save(Notification.builder()
                    .destinataireId("user-1")
                    .type(TypeNotification.INSCRIPTION_CONFIRMEE)
                    .canal(CanalNotification.EMAIL)
                    .titre("Inscription confirmée")
                    .message("Votre inscription à l'événement 'Reboisement Tabarka' a été confirmée.")
                    .referenceId(42L)
                    .build());

            repository.save(Notification.builder()
                    .destinataireId("user-2")
                    .type(TypeNotification.ARBRE_MALADE)
                    .canal(CanalNotification.IN_APP)
                    .titre("Alerte : arbre malade")
                    .message("L'arbre #77 (Pin d'Alep) planté le 01/03/2026 présente des signes de maladie.")
                    .referenceId(77L)
                    .build());

            log.info("3 notifications de démo insérées avec succès.");
        };
    }
}
