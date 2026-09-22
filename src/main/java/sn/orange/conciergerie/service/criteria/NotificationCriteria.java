package sn.orange.conciergerie.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.orange.conciergerie.domain.Notification} entity. This class is used
 * in {@link sn.orange.conciergerie.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter titre;

    private InstantFilter dateEnvoi;

    private BooleanFilter lu;

    private UUIDFilter clientId;

    private UUIDFilter demandeId;

    private Boolean distinct;

    public NotificationCriteria() {}

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.titre = other.optionalTitre().map(StringFilter::copy).orElse(null);
        this.dateEnvoi = other.optionalDateEnvoi().map(InstantFilter::copy).orElse(null);
        this.lu = other.optionalLu().map(BooleanFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(UUIDFilter::copy).orElse(null);
        this.demandeId = other.optionalDemandeId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
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

    public StringFilter getTitre() {
        return titre;
    }

    public Optional<StringFilter> optionalTitre() {
        return Optional.ofNullable(titre);
    }

    public StringFilter titre() {
        if (titre == null) {
            setTitre(new StringFilter());
        }
        return titre;
    }

    public void setTitre(StringFilter titre) {
        this.titre = titre;
    }

    public InstantFilter getDateEnvoi() {
        return dateEnvoi;
    }

    public Optional<InstantFilter> optionalDateEnvoi() {
        return Optional.ofNullable(dateEnvoi);
    }

    public InstantFilter dateEnvoi() {
        if (dateEnvoi == null) {
            setDateEnvoi(new InstantFilter());
        }
        return dateEnvoi;
    }

    public void setDateEnvoi(InstantFilter dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public BooleanFilter getLu() {
        return lu;
    }

    public Optional<BooleanFilter> optionalLu() {
        return Optional.ofNullable(lu);
    }

    public BooleanFilter lu() {
        if (lu == null) {
            setLu(new BooleanFilter());
        }
        return lu;
    }

    public void setLu(BooleanFilter lu) {
        this.lu = lu;
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
        final NotificationCriteria that = (NotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titre, that.titre) &&
            Objects.equals(dateEnvoi, that.dateEnvoi) &&
            Objects.equals(lu, that.lu) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(demandeId, that.demandeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titre, dateEnvoi, lu, clientId, demandeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitre().map(f -> "titre=" + f + ", ").orElse("") +
            optionalDateEnvoi().map(f -> "dateEnvoi=" + f + ", ").orElse("") +
            optionalLu().map(f -> "lu=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalDemandeId().map(f -> "demandeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
