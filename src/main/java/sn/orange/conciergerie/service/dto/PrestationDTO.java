package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.Prestation} entity.
 */
@Schema(description = "Exécution effective du service.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrestationDTO implements Serializable {

    private UUID id;

    private Instant dateDebut;

    private Instant dateFin;

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

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
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
        if (!(o instanceof PrestationDTO)) {
            return false;
        }

        PrestationDTO prestationDTO = (PrestationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prestationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrestationDTO{" +
            "id='" + getId() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", demande=" + getDemande() +
            ", partenaire=" + getPartenaire() +
            "}";
    }
}
