package sn.orange.conciergerie.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.orange.conciergerie.domain.ConsommationQuota} entity. This class is used
 * in {@link sn.orange.conciergerie.web.rest.ConsommationQuotaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /consommation-quotas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsommationQuotaCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private BigDecimalFilter quantite;

    private InstantFilter dateConsommation;

    private UUIDFilter clientId;

    private UUIDFilter quotaServiceId;

    private UUIDFilter quotaDetailId;

    private Boolean distinct;

    public ConsommationQuotaCriteria() {}

    public ConsommationQuotaCriteria(ConsommationQuotaCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.quantite = other.optionalQuantite().map(BigDecimalFilter::copy).orElse(null);
        this.dateConsommation = other.optionalDateConsommation().map(InstantFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(UUIDFilter::copy).orElse(null);
        this.quotaServiceId = other.optionalQuotaServiceId().map(UUIDFilter::copy).orElse(null);
        this.quotaDetailId = other.optionalQuotaDetailId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ConsommationQuotaCriteria copy() {
        return new ConsommationQuotaCriteria(this);
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

    public BigDecimalFilter getQuantite() {
        return quantite;
    }

    public Optional<BigDecimalFilter> optionalQuantite() {
        return Optional.ofNullable(quantite);
    }

    public BigDecimalFilter quantite() {
        if (quantite == null) {
            setQuantite(new BigDecimalFilter());
        }
        return quantite;
    }

    public void setQuantite(BigDecimalFilter quantite) {
        this.quantite = quantite;
    }

    public InstantFilter getDateConsommation() {
        return dateConsommation;
    }

    public Optional<InstantFilter> optionalDateConsommation() {
        return Optional.ofNullable(dateConsommation);
    }

    public InstantFilter dateConsommation() {
        if (dateConsommation == null) {
            setDateConsommation(new InstantFilter());
        }
        return dateConsommation;
    }

    public void setDateConsommation(InstantFilter dateConsommation) {
        this.dateConsommation = dateConsommation;
    }

    public UUIDFilter getClientId() {
        return clientId;
    }

    public Optional<UUIDFilter> optionalClientId() {
        return Optional.ofNullable(clientId);
    }

    public UUIDFilter clientId() {
        if (clientId == null) {
            setClientId(new UUIDFilter());
        }
        return clientId;
    }

    public void setClientId(UUIDFilter clientId) {
        this.clientId = clientId;
    }

    public UUIDFilter getQuotaServiceId() {
        return quotaServiceId;
    }

    public Optional<UUIDFilter> optionalQuotaServiceId() {
        return Optional.ofNullable(quotaServiceId);
    }

    public UUIDFilter quotaServiceId() {
        if (quotaServiceId == null) {
            setQuotaServiceId(new UUIDFilter());
        }
        return quotaServiceId;
    }

    public void setQuotaServiceId(UUIDFilter quotaServiceId) {
        this.quotaServiceId = quotaServiceId;
    }

    public UUIDFilter getQuotaDetailId() {
        return quotaDetailId;
    }

    public Optional<UUIDFilter> optionalQuotaDetailId() {
        return Optional.ofNullable(quotaDetailId);
    }

    public UUIDFilter quotaDetailId() {
        if (quotaDetailId == null) {
            setQuotaDetailId(new UUIDFilter());
        }
        return quotaDetailId;
    }

    public void setQuotaDetailId(UUIDFilter quotaDetailId) {
        this.quotaDetailId = quotaDetailId;
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
        final ConsommationQuotaCriteria that = (ConsommationQuotaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(quantite, that.quantite) &&
            Objects.equals(dateConsommation, that.dateConsommation) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(quotaServiceId, that.quotaServiceId) &&
            Objects.equals(quotaDetailId, that.quotaDetailId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantite, dateConsommation, clientId, quotaServiceId, quotaDetailId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsommationQuotaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalQuantite().map(f -> "quantite=" + f + ", ").orElse("") +
            optionalDateConsommation().map(f -> "dateConsommation=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalQuotaServiceId().map(f -> "quotaServiceId=" + f + ", ").orElse("") +
            optionalQuotaDetailId().map(f -> "quotaDetailId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
