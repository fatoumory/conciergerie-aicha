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
import sn.orange.conciergerie.domain.enumeration.TypeMouvementStock;

/**
 * Mouvement de crédit/débit du stock (TransactionAccount dans le document).
 */
@Entity
@Table(name = "mouvement_stock")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MouvementStock implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeMouvementStock type;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "quantite", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantite;

    @NotNull
    @Column(name = "date_transaction", nullable = false)
    private Instant dateTransaction;

    @Column(name = "motif")
    private String motif;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "service" }, allowSetters = true)
    private CompteStock compteStock;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public MouvementStock id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TypeMouvementStock getType() {
        return this.type;
    }

    public MouvementStock type(TypeMouvementStock type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeMouvementStock type) {
        this.type = type;
    }

    public BigDecimal getQuantite() {
        return this.quantite;
    }

    public MouvementStock quantite(BigDecimal quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public Instant getDateTransaction() {
        return this.dateTransaction;
    }

    public MouvementStock dateTransaction(Instant dateTransaction) {
        this.setDateTransaction(dateTransaction);
        return this;
    }

    public void setDateTransaction(Instant dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public String getMotif() {
        return this.motif;
    }

    public MouvementStock motif(String motif) {
        this.setMotif(motif);
        return this;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public CompteStock getCompteStock() {
        return this.compteStock;
    }

    public void setCompteStock(CompteStock compteStock) {
        this.compteStock = compteStock;
    }

    public MouvementStock compteStock(CompteStock compteStock) {
        this.setCompteStock(compteStock);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MouvementStock)) {
            return false;
        }
        return getId() != null && getId().equals(((MouvementStock) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MouvementStock{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", quantite=" + getQuantite() +
            ", dateTransaction='" + getDateTransaction() + "'" +
            ", motif='" + getMotif() + "'" +
            "}";
    }
}
