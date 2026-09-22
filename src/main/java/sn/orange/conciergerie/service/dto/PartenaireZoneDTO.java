package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.PartenaireZone} entity.
 */
@Schema(description = "Zones d'intervention d'un partenaire.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartenaireZoneDTO implements Serializable {

    private UUID id;

    @NotNull
    private PartenaireDTO partenaire;

    @NotNull
    private ZoneDTO zone;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PartenaireDTO getPartenaire() {
        return partenaire;
    }

    public void setPartenaire(PartenaireDTO partenaire) {
        this.partenaire = partenaire;
    }

    public ZoneDTO getZone() {
        return zone;
    }

    public void setZone(ZoneDTO zone) {
        this.zone = zone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartenaireZoneDTO)) {
            return false;
        }

        PartenaireZoneDTO partenaireZoneDTO = (PartenaireZoneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, partenaireZoneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartenaireZoneDTO{" +
            "id='" + getId() + "'" +
            ", partenaire=" + getPartenaire() +
            ", zone=" + getZone() +
            "}";
    }
}
