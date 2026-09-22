package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.CompteStock} entity.
 */
@Schema(description = "Stock ou capacité globale d'un service (Account dans le document).")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteStockDTO implements Serializable {

    private UUID id;

    @NotNull
    private BigDecimal solde;

    @NotNull
    private ServiceConciergerieDTO service;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
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
        if (!(o instanceof CompteStockDTO)) {
            return false;
        }

        CompteStockDTO compteStockDTO = (CompteStockDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compteStockDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteStockDTO{" +
            "id='" + getId() + "'" +
            ", solde=" + getSolde() +
            ", service=" + getService() +
            "}";
    }
}
