package com.reboistn.event.dto;

import com.reboistn.event.entity.Evenement;
import com.reboistn.event.enums.StatutEvenement;
import com.reboistn.event.enums.TypeEvenement;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Représentation détaillée d'un événement")
public class EvenementResponse {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Titre de l'événement", example = "Grande plantation citoyenne au Belvédère")
    private String titre;

    @Schema(description = "Description de l'événement")
    private String description;

    @Schema(description = "Type d'événement", example = "PLANTATION")
    private TypeEvenement typeEvenement;

    @Schema(description = "Date de début")
    private LocalDateTime dateDebut;

    @Schema(description = "Date de fin")
    private LocalDateTime dateFin;

    @Schema(description = "Lieu du rassemblement", example = "Parc du Belvédère")
    private String lieu;

    @Schema(description = "Ville", example = "Tunis")
    private String ville;

    @Schema(description = "Latitude", example = "36.8233")
    private Double latitude;

    @Schema(description = "Longitude", example = "10.1714")
    private Double longitude;

    @Schema(description = "Nombre total de places", example = "100")
    private Integer nombrePlaces;

    @Schema(description = "Nombre de bénévoles actuellement inscrits", example = "25")
    private Integer nombreInscrits;

    @Schema(description = "Nombre de places encore disponibles", example = "75")
    private Integer placesRestantes;

    @Schema(description = "URL de l'image")
    private String imageUrl;

    @Schema(description = "Statut de l'événement", example = "PUBLIE")
    private StatutEvenement statut;

    @Schema(description = "Identifiant de l'organisateur (MS2)", example = "1")
    private Long organisateurId;

    @Schema(description = "Date de création de la fiche")
    private LocalDateTime dateCreation;

    @Schema(description = "Date de dernière modification")
    private LocalDateTime dateModification;

    public EvenementResponse() {
    }

    public static EvenementResponse fromEntity(Evenement evenement) {
        if (evenement == null) {
            return null;
        }
        EvenementResponse response = new EvenementResponse();
        response.setId(evenement.getId());
        response.setTitre(evenement.getTitre());
        response.setDescription(evenement.getDescription());
        response.setTypeEvenement(evenement.getTypeEvenement());
        response.setDateDebut(evenement.getDateDebut());
        response.setDateFin(evenement.getDateFin());
        response.setLieu(evenement.getLieu());
        response.setVille(evenement.getVille());
        response.setLatitude(evenement.getLatitude());
        response.setLongitude(evenement.getLongitude());
        response.setNombrePlaces(evenement.getNombrePlaces());
        response.setNombreInscrits(evenement.getNombreInscrits());
        int restant = (evenement.getNombrePlaces() != null ? evenement.getNombrePlaces() : 0)
                - (evenement.getNombreInscrits() != null ? evenement.getNombreInscrits() : 0);
        response.setPlacesRestantes(Math.max(0, restant));
        response.setImageUrl(evenement.getImageUrl());
        response.setStatut(evenement.getStatut());
        response.setOrganisateurId(evenement.getOrganisateurId());
        response.setDateCreation(evenement.getDateCreation());
        response.setDateModification(evenement.getDateModification());
        return response;
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

    public Integer getPlacesRestantes() {
        return placesRestantes;
    }

    public void setPlacesRestantes(Integer placesRestantes) {
        this.placesRestantes = placesRestantes;
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
