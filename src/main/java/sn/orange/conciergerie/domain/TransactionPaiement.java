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
import sn.orange.conciergerie.domain.enumeration.ModePaiement;
import sn.orange.conciergerie.domain.enumeration.StatutTransaction;

/**
 * Tentative de paiement d'une demande (Transaction dans le document).
 */
@Entity
@Table(name = "transaction_paiement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionPaiement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant", precision = 21, scale = 2, nullable = false)
    private BigDecimal montant;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mode_paiement", nullable = false)
    private ModePaiement modePaiement;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutTransaction statut;

    @Column(name = "reference_externe")
    private String referenceExterne;

    @NotNull
    @Column(name = "date_transaction", nullable = false)
    private Instant dateTransaction;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "client", "service", "typeDemande", "statut", "codePromo", "prestation", "codeQrService", "facture", "evaluation" },
        allowSetters = true
    )
    private Demande demande;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public TransactionPaiement id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getMontant() {
        return this.montant;
    }

    public TransactionPaiement montant(BigDecimal montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public ModePaiement getModePaiement() {
        return this.modePaiement;
    }

    public TransactionPaiement modePaiement(ModePaiement modePaiement) {
        this.setModePaiement(modePaiement);
        return this;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public StatutTransaction getStatut() {
        return this.statut;
    }

    public TransactionPaiement statut(StatutTransaction statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutTransaction statut) {
        this.statut = statut;
    }

    public String getReferenceExterne() {
        return this.referenceExterne;
    }

    public TransactionPaiement referenceExterne(String referenceExterne) {
        this.setReferenceExterne(referenceExterne);
        return this;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public Instant getDateTransaction() {
        return this.dateTransaction;
    }

    public TransactionPaiement dateTransaction(Instant dateTransaction) {
        this.setDateTransaction(dateTransaction);
        return this;
    }

    public void setDateTransaction(Instant dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public Demande getDemande() {
        return this.demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public TransactionPaiement demande(Demande demande) {
        this.setDemande(demande);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionPaiement)) {
            return false;
        }
        return getId() != null && getId().equals(((TransactionPaiement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionPaiement{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", modePaiement='" + getModePaiement() + "'" +
            ", statut='" + getStatut() + "'" +
            ", referenceExterne='" + getReferenceExterne() + "'" +
            ", dateTransaction='" + getDateTransaction() + "'" +
            "}";
    }
}
