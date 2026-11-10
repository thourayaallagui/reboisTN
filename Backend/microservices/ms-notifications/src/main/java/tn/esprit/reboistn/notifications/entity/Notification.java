package tn.esprit.reboistn.notifications.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.reboistn.notifications.enums.CanalNotification;
import tn.esprit.reboistn.notifications.enums.TypeNotification;

import java.time.LocalDateTime;

/**
 * Entité JPA représentant une notification destinée à un utilisateur.
 */
@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identifiant de l'utilisateur destinataire (référence logique vers ms-utilisateurs). */
    @Column(nullable = false)
    private String destinataireId;

    /** Type métier de la notification. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeNotification type;

    /** Canal de diffusion (EMAIL ou IN_APP). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalNotification canal;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, length = 1000)
    private String message;

    /** Indique si la notification a été lue par le destinataire. */
    @Column(nullable = false)
    @Builder.Default
    private boolean lue = false;

    /** Date de création, remplie automatiquement. */
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    /** Date d'envoi effective (nullable, pertinent pour les notifications EMAIL). */
    private LocalDateTime dateEnvoi;

    /** Référence vers l'objet métier concerné (id événement, arbre, etc.). */
    private Long referenceId;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }
}
