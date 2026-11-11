package com.reboistn.event.dto;

import com.reboistn.event.enums.TypeEvenement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Données requises pour créer ou modifier un événement")
public class EvenementRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Schema(description = "Titre de l'événement", example = "Grande plantation citoyenne au Belvédère")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    @Schema(description = "Description détaillée des activités", example = "Reboisement et plantation d'arbres méditerranéens.")
    private String description;

    @NotNull(message = "Le type d'événement est obligatoire")
    @Schema(description = "Type d'action écologique", example = "PLANTATION")
    private TypeEvenement typeEvenement;

    @NotNull(message = "La date de début est obligatoire")
    @Schema(description = "Date et heure de début", example = "2026-11-20T09:00:00")
    private LocalDateTime dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @Schema(description = "Date et heure de fin", example = "2026-11-20T14:00:00")
    private LocalDateTime dateFin;

    @NotBlank(message = "Le lieu est obligatoire")
    @Schema(description = "Lieu précis du rassemblement", example = "Parc du Belvédère")
    private String lieu;

    @NotBlank(message = "La ville est obligatoire")
    @Schema(description = "Ville ou gouvernorat", example = "Tunis")
    private String ville;

    @Schema(description = "Latitude géographique", example = "36.8233")
    private Double latitude;

    @Schema(description = "Longitude géographique", example = "10.1714")
    private Double longitude;

    @NotNull(message = "Le nombre de places est obligatoire")
    @Min(value = 1, message = "Le nombre de places doit être strictement positif")
    @Schema(description = "Capacité d'accueil maximale en bénévoles", example = "100")
    private Integer nombrePlaces;

    @Schema(description = "URL de l'affiche ou photo d'illustration", example = "https://images.unsplash.com/photo-1542601906990-b4d3fb778b09")
    private String imageUrl;

    @NotNull(message = "L'identifiant de l'organisateur est obligatoire")
    @Schema(description = "ID externe de l'organisateur (MS2 Utilisateurs)", example = "1")
    private Long organisateurId;

    public EvenementRequest() {
    }

    public EvenementRequest(String titre, String description, TypeEvenement typeEvenement,
                            LocalDateTime dateDebut, LocalDateTime dateFin, String lieu, String ville,
                            Double latitude, Double longitude, Integer nombrePlaces, String imageUrl,
                            Long organisateurId) {
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
        this.imageUrl = imageUrl;
        this.organisateurId = organisateurId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getOrganisateurId() {
        return organisateurId;
    }

    public void setOrganisateurId(Long organisateurId) {
        this.organisateurId = organisateurId;
    }
}
