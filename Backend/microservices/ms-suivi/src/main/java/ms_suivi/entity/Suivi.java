package ms_suivi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.FetchType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suivi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suivi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Arbre ID is required")
    @Positive(message = "Arbre ID must be positive")
    @Column(name = "arbre_id", nullable = false)
    private Long arbreId;

    @NotNull(message = "Date de suivi is required")
    @PastOrPresent(message = "Date de suivi cannot be in the future")
    @Column(name = "date_suivi", nullable = false)
    private LocalDate dateSuivi;

    @DecimalMin(value = "0.0", message = "Hauteur must be at least 0.0")
    private Double hauteur;

    @DecimalMin(value = "0.0", message = "Diamètre tronc must be at least 0.0")
    private Double diametreTronc;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_sante")
    private EtatSante etatSante;

    @Size(max = 500, message = "Observation must not exceed 500 characters")
    private String observation;

    @OneToMany(mappedBy = "suivi", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @Valid
    private List<Intervention> interventions = new ArrayList<>();

    @OneToMany(mappedBy = "suivi", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @Valid
    private List<PhotoSuivi> photos = new ArrayList<>();
}
