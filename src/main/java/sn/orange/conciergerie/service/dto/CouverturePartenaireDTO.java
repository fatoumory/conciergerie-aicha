package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.CouverturePartenaire} entity.
 */
@Schema(description = "Services qu'un partenaire peut fournir.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CouverturePartenaireDTO implements Serializable {

    private UUID id;

    @NotNull
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @NotNull
    private PartenaireDTO partenaire;

    @NotNull
    private ServiceConciergerieDTO service;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public PartenaireDTO getPartenaire() {
        return partenaire;
    }

    public void setPartenaire(PartenaireDTO partenaire) {
        this.partenaire = partenaire;
    }

    public ServiceConciergerieDTO getService() {
        return service;
    }

    public void setService(ServiceConciergerieDTO service) {
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CouverturePartenaireDTO)) {
            return false;
        }

        CouverturePartenaireDTO couverturePartenaireDTO = (CouverturePartenaireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, couverturePartenaireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CouverturePartenaireDTO{" +
            "id='" + getId() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", partenaire=" + getPartenaire() +
            ", service=" + getService() +
            "}";
    }
}
