package sn.orange.conciergerie.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Historique des changements de statut.
 */
@Entity
@Table(name = "historique_statut_demande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueStatutDemande implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "date_changement", nullable = false)
    private Instant dateChangement;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "client", "service", "typeDemande", "statut", "codePromo", "prestation", "codeQrService", "facture", "evaluation" },
        allowSetters = true
    )
    private Demande demande;

    @ManyToOne(optional = false)
    @NotNull
    private StatutDemande statut;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public HistoriqueStatutDemande id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getDateChangement() {
        return this.dateChangement;
    }

    public HistoriqueStatutDemande dateChangement(Instant dateChangement) {
        this.setDateChangement(dateChangement);
        return this;
    }

    public void setDateChangement(Instant dateChangement) {
        this.dateChangement = dateChangement;
    }

    public Demande getDemande() {
        return this.demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public HistoriqueStatutDemande demande(Demande demande) {
        this.setDemande(demande);
        return this;
    }

    public StatutDemande getStatut() {
        return this.statut;
    }

    public void setStatut(StatutDemande statutDemande) {
        this.statut = statutDemande;
    }

    public HistoriqueStatutDemande statut(StatutDemande statutDemande) {
        this.setStatut(statutDemande);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueStatutDemande)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoriqueStatutDemande) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueStatutDemande{" +
            "id=" + getId() +
            ", dateChangement='" + getDateChangement() + "'" +
            "}";
    }
}
