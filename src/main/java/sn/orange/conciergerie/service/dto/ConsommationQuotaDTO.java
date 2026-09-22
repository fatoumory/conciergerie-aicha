package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.ConsommationQuota} entity.
 */
@Schema(description = "Consommation réelle d'un quota par un client.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsommationQuotaDTO implements Serializable {

    private UUID id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal quantite;

    @NotNull
    private Instant dateConsommation;

    @NotNull
    private ClientDTO client;

    @NotNull
    private QuotaServiceDTO quotaService;

    private QuotaDetailDTO quotaDetail;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public Instant getDateConsommation() {
        return dateConsommation;
    }

    public void setDateConsommation(Instant dateConsommation) {
        this.dateConsommation = dateConsommation;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public QuotaServiceDTO getQuotaService() {
        return quotaService;
    }

    public void setQuotaService(QuotaServiceDTO quotaService) {
        this.quotaService = quotaService;
    }

    public QuotaDetailDTO getQuotaDetail() {
        return quotaDetail;
    }

    public void setQuotaDetail(QuotaDetailDTO quotaDetail) {
        this.quotaDetail = quotaDetail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsommationQuotaDTO)) {
            return false;
        }

        ConsommationQuotaDTO consommationQuotaDTO = (ConsommationQuotaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consommationQuotaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsommationQuotaDTO{" +
            "id='" + getId() + "'" +
            ", quantite=" + getQuantite() +
            ", dateConsommation='" + getDateConsommation() + "'" +
            ", client=" + getClient() +
            ", quotaService=" + getQuotaService() +
            ", quotaDetail=" + getQuotaDetail() +
            "}";
    }
}
