package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.AffectationDemande} entity.
 */
@Schema(description = "Affectation / réaffectation à un partenaire.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffectationDemandeDTO implements Serializable {

    private UUID id;

    @NotNull
    private Instant dateAffectation;

    @NotNull
    private DemandeDTO demande;

    @NotNull
    private PartenaireDTO partenaire;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getDateAffectation() {
        return dateAffectation;
    }

    public void setDateAffectation(Instant dateAffectation) {
        this.dateAffectation = dateAffectation;
    }

    public DemandeDTO getDemande() {
        return demande;
    }

    public void setDemande(DemandeDTO demande) {
        this.demande = demande;
    }

    public PartenaireDTO getPartenaire() {
        return partenaire;
    }

    public void setPartenaire(PartenaireDTO partenaire) {
        this.partenaire = partenaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffectationDemandeDTO)) {
            return false;
        }

        AffectationDemandeDTO affectationDemandeDTO = (AffectationDemandeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, affectationDemandeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffectationDemandeDTO{" +
            "id='" + getId() + "'" +
            ", dateAffectation='" + getDateAffectation() + "'" +
            ", demande=" + getDemande() +
            ", partenaire=" + getPartenaire() +
            "}";
    }
}
