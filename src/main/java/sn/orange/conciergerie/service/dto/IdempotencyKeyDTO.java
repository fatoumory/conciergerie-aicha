package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.IdempotencyKey} entity.
 */
@Schema(description = "Empêche l'exécution multiple d'une même opération.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdempotencyKeyDTO implements Serializable {

    private UUID id;

    @NotNull
    private String cle;

    @NotNull
    private String typeOperation;

    private UUID resourceId;

    @NotNull
    private Instant dateCreation;

    private Instant dateExpiration;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public UUID getResourceId() {
        return resourceId;
    }

    public void setResourceId(UUID resourceId) {
        this.resourceId = resourceId;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Instant dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdempotencyKeyDTO)) {
            return false;
        }

        IdempotencyKeyDTO idempotencyKeyDTO = (IdempotencyKeyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, idempotencyKeyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdempotencyKeyDTO{" +
            "id='" + getId() + "'" +
            ", cle='" + getCle() + "'" +
            ", typeOperation='" + getTypeOperation() + "'" +
            ", resourceId='" + getResourceId() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateExpiration='" + getDateExpiration() + "'" +
            "}";
    }
}
