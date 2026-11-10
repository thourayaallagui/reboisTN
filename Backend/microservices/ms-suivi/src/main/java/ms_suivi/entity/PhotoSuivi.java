package ms_suivi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Entity
@Table(name = "photo_suivi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoSuivi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Suivi is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suivi_id", nullable = false)
    @JsonBackReference
    private Suivi suivi;

    @NotBlank(message = "URL photo cannot be blank")
    @URL(message = "URL photo must be a valid URL")
    @Size(max = 500, message = "URL photo must not exceed 500 characters")
    @Column(name = "url_photo", nullable = false, length = 500)
    private String urlPhoto;

    @NotNull(message = "Date d'ajout is required")
    @PastOrPresent(message = "Date d'ajout cannot be in the future")
    @Column(name = "date_ajout", nullable = false)
    private LocalDate dateAjout;
}