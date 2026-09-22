package sn.orange.conciergerie.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.orange.conciergerie.domain.Demande} entity. This class is used
 * in {@link sn.orange.conciergerie.web.rest.DemandeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /demandes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DemandeCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private InstantFilter dateCreation;

    private StringFilter description;

    private UUIDFilter clientId;

    private UUIDFilter serviceId;

    private UUIDFilter typeDemandeId;

    private UUIDFilter statutId;

    private UUIDFilter codePromoId;

    private UUIDFilter prestationId;

    private UUIDFilter codeQrServiceId;

    private UUIDFilter factureId;

    private UUIDFilter evaluationId;

    private Boolean distinct;

    public DemandeCriteria() {}

    public DemandeCriteria(DemandeCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.dateCreation = other.optionalDateCreation().map(InstantFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(UUIDFilter::copy).orElse(null);
        this.serviceId = other.optionalServiceId().map(UUIDFilter::copy).orElse(null);
        this.typeDemandeId = other.optionalTypeDemandeId().map(UUIDFilter::copy).orElse(null);
        this.statutId = other.optionalStatutId().map(UUIDFilter::copy).orElse(null);
        this.codePromoId = other.optionalCodePromoId().map(UUIDFilter::copy).orElse(null);
        this.prestationId = other.optionalPrestationId().map(UUIDFilter::copy).orElse(null);
        this.codeQrServiceId = other.optionalCodeQrServiceId().map(UUIDFilter::copy).orElse(null);
        this.factureId = other.optionalFactureId().map(UUIDFilter::copy).orElse(null);
        this.evaluationId = other.optionalEvaluationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DemandeCriteria copy() {
        return new DemandeCriteria(this);
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

    public InstantFilter getDateCreation() {
        return dateCreation;
    }

    public Optional<InstantFilter> optionalDateCreation() {
        return Optional.ofNullable(dateCreation);
    }

    public InstantFilter dateCreation() {
        if (dateCreation == null) {
            setDateCreation(new InstantFilter());
        }
        return dateCreation;
    }

    public void setDateCreation(InstantFilter dateCreation) {
        this.dateCreation = dateCreation;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public UUIDFilter getServiceId() {
        return serviceId;
    }

    public Optional<UUIDFilter> optionalServiceId() {
        return Optional.ofNullable(serviceId);
    }

    public UUIDFilter serviceId() {
        if (serviceId == null) {
            setServiceId(new UUIDFilter());
        }
        return serviceId;
    }

    public void setServiceId(UUIDFilter serviceId) {
        this.serviceId = serviceId;
    }

    public UUIDFilter getTypeDemandeId() {
        return typeDemandeId;
    }

    public Optional<UUIDFilter> optionalTypeDemandeId() {
        return Optional.ofNullable(typeDemandeId);
    }

    public UUIDFilter typeDemandeId() {
        if (typeDemandeId == null) {
            setTypeDemandeId(new UUIDFilter());
        }
        return typeDemandeId;
    }

    public void setTypeDemandeId(UUIDFilter typeDemandeId) {
        this.typeDemandeId = typeDemandeId;
    }

    public UUIDFilter getStatutId() {
        return statutId;
    }

    public Optional<UUIDFilter> optionalStatutId() {
        return Optional.ofNullable(statutId);
    }

    public UUIDFilter statutId() {
        if (statutId == null) {
            setStatutId(new UUIDFilter());
        }
        return statutId;
    }

    public void setStatutId(UUIDFilter statutId) {
        this.statutId = statutId;
    }

    public UUIDFilter getCodePromoId() {
        return codePromoId;
    }

    public Optional<UUIDFilter> optionalCodePromoId() {
        return Optional.ofNullable(codePromoId);
    }

    public UUIDFilter codePromoId() {
        if (codePromoId == null) {
            setCodePromoId(new UUIDFilter());
        }
        return codePromoId;
    }

    public void setCodePromoId(UUIDFilter codePromoId) {
        this.codePromoId = codePromoId;
    }

    public UUIDFilter getPrestationId() {
        return prestationId;
    }

    public Optional<UUIDFilter> optionalPrestationId() {
        return Optional.ofNullable(prestationId);
    }

    public UUIDFilter prestationId() {
        if (prestationId == null) {
            setPrestationId(new UUIDFilter());
        }
        return prestationId;
    }

    public void setPrestationId(UUIDFilter prestationId) {
        this.prestationId = prestationId;
    }

    public UUIDFilter getCodeQrServiceId() {
        return codeQrServiceId;
    }

    public Optional<UUIDFilter> optionalCodeQrServiceId() {
        return Optional.ofNullable(codeQrServiceId);
    }

    public UUIDFilter codeQrServiceId() {
        if (codeQrServiceId == null) {
            setCodeQrServiceId(new UUIDFilter());
        }
        return codeQrServiceId;
    }

    public void setCodeQrServiceId(UUIDFilter codeQrServiceId) {
        this.codeQrServiceId = codeQrServiceId;
    }

    public UUIDFilter getFactureId() {
        return factureId;
    }

    public Optional<UUIDFilter> optionalFactureId() {
        return Optional.ofNullable(factureId);
    }

    public UUIDFilter factureId() {
        if (factureId == null) {
            setFactureId(new UUIDFilter());
        }
        return factureId;
    }

    public void setFactureId(UUIDFilter factureId) {
        this.factureId = factureId;
    }

    public UUIDFilter getEvaluationId() {
        return evaluationId;
    }

    public Optional<UUIDFilter> optionalEvaluationId() {
        return Optional.ofNullable(evaluationId);
    }

    public UUIDFilter evaluationId() {
        if (evaluationId == null) {
            setEvaluationId(new UUIDFilter());
        }
        return evaluationId;
    }

    public void setEvaluationId(UUIDFilter evaluationId) {
        this.evaluationId = evaluationId;
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
        final DemandeCriteria that = (DemandeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(description, that.description) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(serviceId, that.serviceId) &&
            Objects.equals(typeDemandeId, that.typeDemandeId) &&
            Objects.equals(statutId, that.statutId) &&
            Objects.equals(codePromoId, that.codePromoId) &&
            Objects.equals(prestationId, that.prestationId) &&
            Objects.equals(codeQrServiceId, that.codeQrServiceId) &&
            Objects.equals(factureId, that.factureId) &&
            Objects.equals(evaluationId, that.evaluationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dateCreation,
            description,
            clientId,
            serviceId,
            typeDemandeId,
            statutId,
            codePromoId,
            prestationId,
            codeQrServiceId,
            factureId,
            evaluationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDateCreation().map(f -> "dateCreation=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalServiceId().map(f -> "serviceId=" + f + ", ").orElse("") +
            optionalTypeDemandeId().map(f -> "typeDemandeId=" + f + ", ").orElse("") +
            optionalStatutId().map(f -> "statutId=" + f + ", ").orElse("") +
            optionalCodePromoId().map(f -> "codePromoId=" + f + ", ").orElse("") +
            optionalPrestationId().map(f -> "prestationId=" + f + ", ").orElse("") +
            optionalCodeQrServiceId().map(f -> "codeQrServiceId=" + f + ", ").orElse("") +
            optionalFactureId().map(f -> "factureId=" + f + ", ").orElse("") +
            optionalEvaluationId().map(f -> "evaluationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
