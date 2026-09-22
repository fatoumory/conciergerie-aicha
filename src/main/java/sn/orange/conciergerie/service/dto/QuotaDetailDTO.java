package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.QuotaDetail} entity.
 */
@Schema(description = "Décomposition d'un quota par zone (Dakar, Banlieue...).")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotaDetailDTO implements Serializable {

    private UUID id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal limite;

    @NotNull
    private QuotaServiceDTO quotaService;

    @NotNull
    private ZoneDTO zone;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public QuotaServiceDTO getQuotaService() {
        return quotaService;
    }

    public void setQuotaService(QuotaServiceDTO quotaService) {
        this.quotaService = quotaService;
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
        if (!(o instanceof QuotaDetailDTO)) {
            return false;
        }

        QuotaDetailDTO quotaDetailDTO = (QuotaDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quotaDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotaDetailDTO{" +
            "id='" + getId() + "'" +
            ", limite=" + getLimite() +
            ", quotaService=" + getQuotaService() +
            ", zone=" + getZone() +
            "}";
    }
}
