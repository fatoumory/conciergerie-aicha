package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.HistoriqueStatutDemande} entity.
 */
@Schema(description = "Historique des changements de statut.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueStatutDemandeDTO implements Serializable {

    private UUID id;

    @NotNull
    private Instant dateChangement;

    @NotNull
    private DemandeDTO demande;

    @NotNull
    private StatutDemandeDTO statut;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(Instant dateChangement) {
        this.dateChangement = dateChangement;
    }

    public DemandeDTO getDemande() {
        return demande;
    }

    public void setDemande(DemandeDTO demande) {
        this.demande = demande;
    }

    public StatutDemandeDTO getStatut() {
        return statut;
    }

    public void setStatut(StatutDemandeDTO statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueStatutDemandeDTO)) {
            return false;
        }

        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO = (HistoriqueStatutDemandeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historiqueStatutDemandeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueStatutDemandeDTO{" +
            "id='" + getId() + "'" +
            ", dateChangement='" + getDateChangement() + "'" +
            ", demande=" + getDemande() +
            ", statut=" + getStatut() +
            "}";
    }
}
