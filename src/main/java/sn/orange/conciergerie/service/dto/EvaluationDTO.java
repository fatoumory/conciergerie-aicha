package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.Evaluation} entity.
 */
@Schema(description = "Note et commentaire associés à une demande.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluationDTO implements Serializable {

    private UUID id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer note;

    @Lob
    private String commentaire;

    @NotNull
    private Instant dateEvaluation;

    @NotNull
    private DemandeDTO demande;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Instant getDateEvaluation() {
        return dateEvaluation;
    }

    public void setDateEvaluation(Instant dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

    public DemandeDTO getDemande() {
        return demande;
    }

    public void setDemande(DemandeDTO demande) {
        this.demande = demande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvaluationDTO)) {
            return false;
        }

        EvaluationDTO evaluationDTO = (EvaluationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, evaluationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluationDTO{" +
            "id='" + getId() + "'" +
            ", note=" + getNote() +
            ", commentaire='" + getCommentaire() + "'" +
            ", dateEvaluation='" + getDateEvaluation() + "'" +
            ", demande=" + getDemande() +
            "}";
    }
}
