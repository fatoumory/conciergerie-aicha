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
 * Affectation / réaffectation à un partenaire.
 */
@Entity
@Table(name = "affectation_demande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffectationDemande implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "date_affectation", nullable = false)
    private Instant dateAffectation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "client", "service", "typeDemande", "statut", "codePromo", "prestation", "codeQrService", "facture", "evaluation" },
        allowSetters = true
    )
    private Demande demande;

    @ManyToOne(optional = false)
    @NotNull
    private Partenaire partenaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AffectationDemande id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getDateAffectation() {
        return this.dateAffectation;
    }

    public AffectationDemande dateAffectation(Instant dateAffectation) {
        this.setDateAffectation(dateAffectation);
        return this;
    }

    public void setDateAffectation(Instant dateAffectation) {
        this.dateAffectation = dateAffectation;
    }

    public Demande getDemande() {
        return this.demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public AffectationDemande demande(Demande demande) {
        this.setDemande(demande);
        return this;
    }

    public Partenaire getPartenaire() {
        return this.partenaire;
    }

    public void setPartenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
    }

    public AffectationDemande partenaire(Partenaire partenaire) {
        this.setPartenaire(partenaire);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffectationDemande)) {
            return false;
        }
        return getId() != null && getId().equals(((AffectationDemande) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffectationDemande{" +
            "id=" + getId() +
            ", dateAffectation='" + getDateAffectation() + "'" +
            "}";
    }
}
