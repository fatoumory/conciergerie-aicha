package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.JournalAudit} entity.
 */
@Schema(description = "Traçabilité des opérations importantes.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JournalAuditDTO implements Serializable {

    private UUID id;

    @NotNull
    private String action;

    @NotNull
    private String typeObjet;

    private UUID objetId;

    @NotNull
    private String correlationId;

    @NotNull
    private Instant dateAction;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTypeObjet() {
        return typeObjet;
    }

    public void setTypeObjet(String typeObjet) {
        this.typeObjet = typeObjet;
    }

    public UUID getObjetId() {
        return objetId;
    }

    public void setObjetId(UUID objetId) {
        this.objetId = objetId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Instant getDateAction() {
        return dateAction;
    }

    public void setDateAction(Instant dateAction) {
        this.dateAction = dateAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JournalAuditDTO)) {
            return false;
        }

        JournalAuditDTO journalAuditDTO = (JournalAuditDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, journalAuditDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JournalAuditDTO{" +
            "id='" + getId() + "'" +
            ", action='" + getAction() + "'" +
            ", typeObjet='" + getTypeObjet() + "'" +
            ", objetId='" + getObjetId() + "'" +
            ", correlationId='" + getCorrelationId() + "'" +
            ", dateAction='" + getDateAction() + "'" +
            "}";
    }
}
