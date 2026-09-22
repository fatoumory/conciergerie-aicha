package sn.orange.conciergerie.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import sn.orange.conciergerie.domain.enumeration.ModePaiement;
import sn.orange.conciergerie.domain.enumeration.StatutTransaction;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.orange.conciergerie.domain.TransactionPaiement} entity. This class is used
 * in {@link sn.orange.conciergerie.web.rest.TransactionPaiementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transaction-paiements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionPaiementCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ModePaiement
     */
    public static class ModePaiementFilter extends Filter<ModePaiement> {

        public ModePaiementFilter() {}

        public ModePaiementFilter(ModePaiementFilter filter) {
            super(filter);
        }

        @Override
        public ModePaiementFilter copy() {
            return new ModePaiementFilter(this);
        }
    }

    /**
     * Class for filtering StatutTransaction
     */
    public static class StatutTransactionFilter extends Filter<StatutTransaction> {

        public StatutTransactionFilter() {}

        public StatutTransactionFilter(StatutTransactionFilter filter) {
            super(filter);
        }

        @Override
        public StatutTransactionFilter copy() {
            return new StatutTransactionFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private BigDecimalFilter montant;

    private ModePaiementFilter modePaiement;

    private StatutTransactionFilter statut;

    private StringFilter referenceExterne;

    private InstantFilter dateTransaction;

    private UUIDFilter demandeId;

    private Boolean distinct;

    public TransactionPaiementCriteria() {}

    public TransactionPaiementCriteria(TransactionPaiementCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.montant = other.optionalMontant().map(BigDecimalFilter::copy).orElse(null);
        this.modePaiement = other.optionalModePaiement().map(ModePaiementFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutTransactionFilter::copy).orElse(null);
        this.referenceExterne = other.optionalReferenceExterne().map(StringFilter::copy).orElse(null);
        this.dateTransaction = other.optionalDateTransaction().map(InstantFilter::copy).orElse(null);
        this.demandeId = other.optionalDemandeId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransactionPaiementCriteria copy() {
        return new TransactionPaiementCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getMontant() {
        return montant;
    }

    public Optional<BigDecimalFilter> optionalMontant() {
        return Optional.ofNullable(montant);
    }

    public BigDecimalFilter montant() {
        if (montant == null) {
            setMontant(new BigDecimalFilter());
        }
        return montant;
    }

    public void setMontant(BigDecimalFilter montant) {
        this.montant = montant;
    }

    public ModePaiementFilter getModePaiement() {
        return modePaiement;
    }

    public Optional<ModePaiementFilter> optionalModePaiement() {
        return Optional.ofNullable(modePaiement);
    }

    public ModePaiementFilter modePaiement() {
        if (modePaiement == null) {
            setModePaiement(new ModePaiementFilter());
        }
        return modePaiement;
    }

    public void setModePaiement(ModePaiementFilter modePaiement) {
        this.modePaiement = modePaiement;
    }

    public StatutTransactionFilter getStatut() {
        return statut;
    }

    public Optional<StatutTransactionFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutTransactionFilter statut() {
        if (statut == null) {
            setStatut(new StatutTransactionFilter());
        }
        return statut;
    }

    public void setStatut(StatutTransactionFilter statut) {
        this.statut = statut;
    }

    public StringFilter getReferenceExterne() {
        return referenceExterne;
    }

    public Optional<StringFilter> optionalReferenceExterne() {
        return Optional.ofNullable(referenceExterne);
    }

    public StringFilter referenceExterne() {
        if (referenceExterne == null) {
            setReferenceExterne(new StringFilter());
        }
        return referenceExterne;
    }

    public void setReferenceExterne(StringFilter referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public InstantFilter getDateTransaction() {
        return dateTransaction;
    }

    public Optional<InstantFilter> optionalDateTransaction() {
        return Optional.ofNullable(dateTransaction);
    }

    public InstantFilter dateTransaction() {
        if (dateTransaction == null) {
            setDateTransaction(new InstantFilter());
        }
        return dateTransaction;
    }

    public void setDateTransaction(InstantFilter dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public UUIDFilter getDemandeId() {
        return demandeId;
    }

    public Optional<UUIDFilter> optionalDemandeId() {
        return Optional.ofNullable(demandeId);
    }

    public UUIDFilter demandeId() {
        if (demandeId == null) {
            setDemandeId(new UUIDFilter());
        }
        return demandeId;
    }

    public void setDemandeId(UUIDFilter demandeId) {
        this.demandeId = demandeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TransactionPaiementCriteria that = (TransactionPaiementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(modePaiement, that.modePaiement) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(referenceExterne, that.referenceExterne) &&
            Objects.equals(dateTransaction, that.dateTransaction) &&
            Objects.equals(demandeId, that.demandeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, montant, modePaiement, statut, referenceExterne, dateTransaction, demandeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionPaiementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMontant().map(f -> "montant=" + f + ", ").orElse("") +
            optionalModePaiement().map(f -> "modePaiement=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalReferenceExterne().map(f -> "referenceExterne=" + f + ", ").orElse("") +
            optionalDateTransaction().map(f -> "dateTransaction=" + f + ", ").orElse("") +
            optionalDemandeId().map(f -> "demandeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
