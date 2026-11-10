package ms_suivi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "intervention")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Suivi is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suivi_id", nullable = false)
    @JsonBackReference
    private Suivi suivi;

    @NotBlank(message = "Type d'intervention cannot be blank")
    @Size(min = 2, max = 50, message = "Type d'intervention must be between 2 and 50 characters")
    @Column(name = "type_intervention", nullable = false, length = 50)
    private String typeIntervention;

    @NotNull(message = "Date d'intervention is required")
    @PastOrPresent(message = "Date d'intervention cannot be in the future")
    @Column(name = "date_intervention", nullable = false)
    private LocalDate dateIntervention;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(length = 500)
    private String description;
}