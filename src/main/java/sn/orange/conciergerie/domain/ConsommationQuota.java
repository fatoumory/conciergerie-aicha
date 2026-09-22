package sn.orange.conciergerie.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Consommation réelle d'un quota par un client.
 */
@Entity
@Table(name = "consommation_quota")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsommationQuota implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "quantite", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantite;

    @NotNull
    @Column(name = "date_consommation", nullable = false)
    private Instant dateConsommation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "typeClient", "segmentClient" }, allowSetters = true)
    private Client client;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "eligibiliteService" }, allowSetters = true)
    private QuotaService quotaService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quotaService", "zone" }, allowSetters = true)
    private QuotaDetail quotaDetail;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ConsommationQuota id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getQuantite() {
        return this.quantite;
    }

    public ConsommationQuota quantite(BigDecimal quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public Instant getDateConsommation() {
        return this.dateConsommation;
    }

    public ConsommationQuota dateConsommation(Instant dateConsommation) {
        this.setDateConsommation(dateConsommation);
        return this;
    }

    public void setDateConsommation(Instant dateConsommation) {
        this.dateConsommation = dateConsommation;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ConsommationQuota client(Client client) {
        this.setClient(client);
        return this;
    }

    public QuotaService getQuotaService() {
        return this.quotaService;
    }

    public void setQuotaService(QuotaService quotaService) {
        this.quotaService = quotaService;
    }

    public ConsommationQuota quotaService(QuotaService quotaService) {
        this.setQuotaService(quotaService);
        return this;
    }

    public QuotaDetail getQuotaDetail() {
        return this.quotaDetail;
    }

    public void setQuotaDetail(QuotaDetail quotaDetail) {
        this.quotaDetail = quotaDetail;
    }

    public ConsommationQuota quotaDetail(QuotaDetail quotaDetail) {
        this.setQuotaDetail(quotaDetail);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsommationQuota)) {
            return false;
        }
        return getId() != null && getId().equals(((ConsommationQuota) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsommationQuota{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", dateConsommation='" + getDateConsommation() + "'" +
            "}";
    }
}
