package ms_suivi.config;

import lombok.RequiredArgsConstructor;
import ms_suivi.entity.EtatSante;
import ms_suivi.entity.Intervention;
import ms_suivi.entity.PhotoSuivi;
import ms_suivi.entity.Suivi;
import ms_suivi.repository.SuiviRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final SuiviRepository suiviRepository;

    @Override
    public void run(String... args) throws Exception {
        if (suiviRepository.count() == 0) {
            loadSampleData();
        }
    }

    private void loadSampleData() {
        // Suivi 1 - Arbre Olive
        Suivi suivi1 = Suivi.builder()
                .arbreId(1L)
                .dateSuivi(LocalDate.of(2024, 1, 15))
                .hauteur(2.5)
                .diametreTronc(15.0)
                .etatSante(EtatSante.BON)
                .observation("L'arbre est en bonne santé, croissance normale")
                .build();

        Intervention intervention1 = Intervention.builder()
                .suivi(suivi1)
                .typeIntervention("TAILLE")
                .dateIntervention(LocalDate.of(2024, 1, 15))
                .description("Taille de formation pour structurer l'arbre")
                .build();

        PhotoSuivi photo1 = PhotoSuivi.builder()
                .suivi(suivi1)
                .urlPhoto("http://example.com/photos/olive1.jpg")
                .dateAjout(LocalDate.of(2024, 1, 15))
                .build();

        suivi1.getInterventions().add(intervention1);
        suivi1.getPhotos().add(photo1);

        // Suivi 2 - Arbre Olivier (suivi plus tard)
        Suivi suivi2 = Suivi.builder()
                .arbreId(1L)
                .dateSuivi(LocalDate.of(2024, 6, 20))
                .hauteur(2.8)
                .diametreTronc(16.5)
                .etatSante(EtatSante.BON)
                .observation("Croissance excellente après la taille de janvier")
                .build();

        Intervention intervention2 = Intervention.builder()
                .suivi(suivi2)
                .typeIntervention("ARROSAGE")
                .dateIntervention(LocalDate.of(2024, 6, 20))
                .description("Arrosage régulier pendant la période sèche")
                .build();

        suivi2.getInterventions().add(intervention2);

        // Suivi 3 - Arbre Citronnier
        Suivi suivi3 = Suivi.builder()
                .arbreId(2L)
                .dateSuivi(LocalDate.of(2024, 2, 10))
                .hauteur(1.8)
                .diametreTronc(12.0)
                .etatSante(EtatSante.BON)
                .observation("Le citronnier porte beaucoup de fruits")
                .build();

        Intervention intervention3 = Intervention.builder()
                .suivi(suivi3)
                .typeIntervention("FERTILISATION")
                .dateIntervention(LocalDate.of(2024, 2, 10))
                .description("Apport d'engrais organique pour soutenir la fructification")
                .build();

        PhotoSuivi photo3 = PhotoSuivi.builder()
                .suivi(suivi3)
                .urlPhoto("http://example.com/photos/citron1.jpg")
                .dateAjout(LocalDate.of(2024, 2, 10))
                .build();

        suivi3.getInterventions().add(intervention3);
        suivi3.getPhotos().add(photo3);

        // Suivi 4 - Arbre Oranger
        Suivi suivi4 = Suivi.builder()
                .arbreId(3L)
                .dateSuivi(LocalDate.of(2024, 3, 5))
                .hauteur(2.2)
                .diametreTronc(14.0)
                .etatSante(EtatSante.MOYEN)
                .observation("Quelques feuilles jaunes, vérifier l'arrosage")
                .build();

        Intervention intervention4 = Intervention.builder()
                .suivi(suivi4)
                .typeIntervention("TRAITEMENT")
                .dateIntervention(LocalDate.of(2024, 3, 5))
                .description("Traitement préventif contre les parasites")
                .build();

        suivi4.getInterventions().add(intervention4);

        // Suivi 5 - Arbre Figuier
        Suivi suivi5 = Suivi.builder()
                .arbreId(4L)
                .dateSuivi(LocalDate.of(2024, 4, 12))
                .hauteur(3.0)
                .diametreTronc(20.0)
                .etatSante(EtatSante.BON)
                .observation("Figuier en excellente santé, beaucoup de figues")
                .build();

        Intervention intervention5 = Intervention.builder()
                .suivi(suivi5)
                .typeIntervention("TAILLE")
                .dateIntervention(LocalDate.of(2024, 4, 12))
                .description("Taille légère pour améliorer la production")
                .build();

        PhotoSuivi photo5 = PhotoSuivi.builder()
                .suivi(suivi5)
                .urlPhoto("http://example.com/photos/figuier1.jpg")
                .dateAjout(LocalDate.of(2024, 4, 12))
                .build();

        suivi5.getInterventions().add(intervention5);
        suivi5.getPhotos().add(photo5);

        // Save all suivis
        suiviRepository.save(suivi1);
        suiviRepository.save(suivi2);
        suiviRepository.save(suivi3);
        suiviRepository.save(suivi4);
        suiviRepository.save(suivi5);

        System.out.println("✓ Sample data loaded successfully");
    }
}
