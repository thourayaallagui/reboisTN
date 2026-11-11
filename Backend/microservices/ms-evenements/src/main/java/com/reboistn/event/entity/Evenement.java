package com.reboistn.event.entity;

import com.reboistn.event.enums.StatutEvenement;
import com.reboistn.event.enums.TypeEvenement;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "evenements")
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false, length = 200)
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Le type d'événement est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TypeEvenement typeEvenement;

    @NotNull(message = "La date de début est obligatoire")
    @Column(nullable = false)
    private LocalDateTime dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @Column(nullable = false)
    private LocalDateTime dateFin;

    @NotBlank(message = "Le lieu est obligatoire")
    @Column(nullable = false, length = 255)
    private String lieu;

    @NotBlank(message = "La ville est obligatoire")
    @Column(nullable = false, length = 100)
    private String ville;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @NotNull(message = "Le nombre de places est obligatoire")
    @Min(value = 1, message = "Le nombre de places doit être strictement positif")
    @Column(nullable = false)
    private Integer nombrePlaces;

    @Min(value = 0, message = "Le nombre d'inscrits ne peut pas être négatif")
    @Column(nullable = false)
    private Integer nombreInscrits = 0;

    @Column(length = 500)
    private String imageUrl;

    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatutEvenement statut = StatutEvenement.BROUILLON;

    @Column(nullable = false)
    private Long organisateurId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    public Evenement() {
    }

    public Evenement(Long id, String titre, String description, TypeEvenement typeEvenement,
                     LocalDateTime dateDebut, LocalDateTime dateFin, String lieu, String ville,
                     Double latitude, Double longitude, Integer nombrePlaces, Integer nombreInscrits,
                     String imageUrl, StatutEvenement statut, Long organisateurId) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.typeEvenement = typeEvenement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.ville = ville;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nombrePlaces = nombrePlaces;
        this.nombreInscrits = nombreInscrits != null ? nombreInscrits : 0;
        this.imageUrl = imageUrl;
        this.statut = statut != null ? statut : StatutEvenement.BROUILLON;
        this.organisateurId = organisateurId;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.dateCreation == null) {
            this.dateCreation = now;
        }
        if (this.dateModification == null) {
            this.dateModification = now;
        }
        if (this.statut == null) {
            this.statut = StatutEvenement.BROUILLON;
        }
        if (this.nombreInscrits == null) {
            this.nombreInscrits = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.dateModification = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeEvenement getTypeEvenement() {
        return typeEvenement;
    }

    public void setTypeEvenement(TypeEvenement typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(Integer nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    public Integer getNombreInscrits() {
        return nombreInscrits;
    }

    public void setNombreInscrits(Integer nombreInscrits) {
        this.nombreInscrits = nombreInscrits;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StatutEvenement getStatut() {
        return statut;
    }

    public void setStatut(StatutEvenement statut) {
        this.statut = statut;
    }

    public Long getOrganisateurId() {
        return organisateurId;
    }

    public void setOrganisateurId(Long organisateurId) {
        this.organisateurId = organisateurId;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
}
