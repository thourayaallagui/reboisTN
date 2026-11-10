package tn.reboistn.arbres.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Arbre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Référence logique vers l evenement de plantation (ms-evenements). */
    @NotNull
    private Long evenementId;

    /** Référence logique vers l utilisateur qui a planté l arbre (ms-utilisateurs). */
    private Long planteParId;

    @NotNull
    private LocalDate datePlantation;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotBlank
    private String gouvernorat;

    @Enumerated(EnumType.STRING)
    private StatutArbre statut;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "espece_id")
    private Espece espece;
}
