package tn.reboistn.utilisateurs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identifiant du compte dans Keycloak (lien avec l authentification). */
    @NotBlank
    @Column(unique = true)
    private String keycloakId;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    private String telephone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    /** Référence logique vers Organisation (null pour un simple bénévole). */
    private Long organisationId;
}
