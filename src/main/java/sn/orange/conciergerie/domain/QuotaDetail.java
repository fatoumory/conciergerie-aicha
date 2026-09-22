package sn.orange.conciergerie.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Décomposition d'un quota par zone (Dakar, Banlieue...).
 */
@Entity
@Table(name = "quota_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotaDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "limite", precision = 21, scale = 2, nullable = false)
    private BigDecimal limite;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "eligibiliteService" }, allowSetters = true)
    private QuotaService quotaService;

    @ManyToOne(optional = false)
    @NotNull
    private Zone zone;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public QuotaDetail id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getLimite() {
        return this.limite;
    }

    public QuotaDetail limite(BigDecimal limite) {
        this.setLimite(limite);
        return this;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public QuotaService getQuotaService() {
        return this.quotaService;
    }

    public void setQuotaService(QuotaService quotaService) {
        this.quotaService = quotaService;
    }

    public QuotaDetail quotaService(QuotaService quotaService) {
        this.setQuotaService(quotaService);
        return this;
    }

    public Zone getZone() {
        return this.zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public QuotaDetail zone(Zone zone) {
        this.setZone(zone);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotaDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((QuotaDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotaDetail{" +
            "id=" + getId() +
            ", limite=" + getLimite() +
            "}";
    }
}
