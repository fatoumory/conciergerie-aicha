package sn.orange.conciergerie.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Empêche l'exécution multiple d'une même opération.
 */
@Entity
@Table(name = "idempotency_key")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdempotencyKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "cle", nullable = false, unique = true)
    private String cle;

    @NotNull
    @Column(name = "type_operation", nullable = false)
    private String typeOperation;

    @Column(name = "resource_id")
    private UUID resourceId;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @Column(name = "date_expiration")
    private Instant dateExpiration;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public IdempotencyKey id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCle() {
        return this.cle;
    }

    public IdempotencyKey cle(String cle) {
        this.setCle(cle);
        return this;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getTypeOperation() {
        return this.typeOperation;
    }

    public IdempotencyKey typeOperation(String typeOperation) {
        this.setTypeOperation(typeOperation);
        return this;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public UUID getResourceId() {
        return this.resourceId;
    }

    public IdempotencyKey resourceId(UUID resourceId) {
        this.setResourceId(resourceId);
        return this;
    }

    public void setResourceId(UUID resourceId) {
        this.resourceId = resourceId;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public IdempotencyKey dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateExpiration() {
        return this.dateExpiration;
    }

    public IdempotencyKey dateExpiration(Instant dateExpiration) {
        this.setDateExpiration(dateExpiration);
        return this;
    }

    public void setDateExpiration(Instant dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdempotencyKey)) {
            return false;
        }
        return getId() != null && getId().equals(((IdempotencyKey) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdempotencyKey{" +
            "id=" + getId() +
            ", cle='" + getCle() + "'" +
            ", typeOperation='" + getTypeOperation() + "'" +
            ", resourceId='" + getResourceId() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateExpiration='" + getDateExpiration() + "'" +
            "}";
    }
}
