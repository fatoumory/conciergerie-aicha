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
 * Stock ou capacité globale d'un service (Account dans le document).
 */
@Entity
@Table(name = "compte_stock")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteStock implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "solde", precision = 21, scale = 2, nullable = false)
    private BigDecimal solde;

    @JsonIgnoreProperties(value = { "typeService", "compteStock" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private ServiceConciergerie service;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public CompteStock id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getSolde() {
        return this.solde;
    }

    public CompteStock solde(BigDecimal solde) {
        this.setSolde(solde);
        return this;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public ServiceConciergerie getService() {
        return this.service;
    }

    public void setService(ServiceConciergerie serviceConciergerie) {
        this.service = serviceConciergerie;
    }

    public CompteStock service(ServiceConciergerie serviceConciergerie) {
        this.setService(serviceConciergerie);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteStock)) {
            return false;
        }
        return getId() != null && getId().equals(((CompteStock) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteStock{" +
            "id=" + getId() +
            ", solde=" + getSolde() +
            "}";
    }
}
