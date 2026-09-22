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
 * Traçabilité des opérations importantes.
 */
@Entity
@Table(name = "journal_audit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JournalAudit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "action", nullable = false)
    private String action;

    @NotNull
    @Column(name = "type_objet", nullable = false)
    private String typeObjet;

    @Column(name = "objet_id")
    private UUID objetId;

    @NotNull
    @Column(name = "correlation_id", nullable = false)
    private String correlationId;

    @NotNull
    @Column(name = "date_action", nullable = false)
    private Instant dateAction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public JournalAudit id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAction() {
        return this.action;
    }

    public JournalAudit action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTypeObjet() {
        return this.typeObjet;
    }

    public JournalAudit typeObjet(String typeObjet) {
        this.setTypeObjet(typeObjet);
        return this;
    }

    public void setTypeObjet(String typeObjet) {
        this.typeObjet = typeObjet;
    }

    public UUID getObjetId() {
        return this.objetId;
    }

    public JournalAudit objetId(UUID objetId) {
        this.setObjetId(objetId);
        return this;
    }

    public void setObjetId(UUID objetId) {
        this.objetId = objetId;
    }

    public String getCorrelationId() {
        return this.correlationId;
    }

    public JournalAudit correlationId(String correlationId) {
        this.setCorrelationId(correlationId);
        return this;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Instant getDateAction() {
        return this.dateAction;
    }

    public JournalAudit dateAction(Instant dateAction) {
        this.setDateAction(dateAction);
        return this;
    }

    public void setDateAction(Instant dateAction) {
        this.dateAction = dateAction;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JournalAudit)) {
            return false;
        }
        return getId() != null && getId().equals(((JournalAudit) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JournalAudit{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", typeObjet='" + getTypeObjet() + "'" +
            ", objetId='" + getObjetId() + "'" +
            ", correlationId='" + getCorrelationId() + "'" +
            ", dateAction='" + getDateAction() + "'" +
            "}";
    }
}
