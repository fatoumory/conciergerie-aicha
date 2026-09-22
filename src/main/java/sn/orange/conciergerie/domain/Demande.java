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
 * Demande de service formulée par un client.
 */
@Entity
@Table(name = "demande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Demande implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "typeClient", "segmentClient" }, allowSetters = true)
    private Client client;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "typeService", "compteStock" }, allowSetters = true)
    private ServiceConciergerie service;

    @ManyToOne(optional = false)
    @NotNull
    private TypeDemande typeDemande;

    @ManyToOne(optional = false)
    @NotNull
    private StatutDemande statut;

    @ManyToOne(fetch = FetchType.LAZY)
    private CodePromo codePromo;

    @JsonIgnoreProperties(value = { "demande", "partenaire" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "demande")
    private Prestation prestation;

    @JsonIgnoreProperties(value = { "demande" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "demande")
    private CodeQrService codeQrService;

    @JsonIgnoreProperties(value = { "demande" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "demande")
    private Facture facture;

    @JsonIgnoreProperties(value = { "demande" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "demande")
    private Evaluation evaluation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Demande id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public Demande dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDescription() {
        return this.description;
    }

    public Demande description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Demande client(Client client) {
        this.setClient(client);
        return this;
    }

    public ServiceConciergerie getService() {
        return this.service;
    }

    public void setService(ServiceConciergerie serviceConciergerie) {
        this.service = serviceConciergerie;
    }

    public Demande service(ServiceConciergerie serviceConciergerie) {
        this.setService(serviceConciergerie);
        return this;
    }

    public TypeDemande getTypeDemande() {
        return this.typeDemande;
    }

    public void setTypeDemande(TypeDemande typeDemande) {
        this.typeDemande = typeDemande;
    }

    public Demande typeDemande(TypeDemande typeDemande) {
        this.setTypeDemande(typeDemande);
        return this;
    }

    public StatutDemande getStatut() {
        return this.statut;
    }

    public void setStatut(StatutDemande statutDemande) {
        this.statut = statutDemande;
    }

    public Demande statut(StatutDemande statutDemande) {
        this.setStatut(statutDemande);
        return this;
    }

    public CodePromo getCodePromo() {
        return this.codePromo;
    }

    public void setCodePromo(CodePromo codePromo) {
        this.codePromo = codePromo;
    }

    public Demande codePromo(CodePromo codePromo) {
        this.setCodePromo(codePromo);
        return this;
    }

    public Prestation getPrestation() {
        return this.prestation;
    }

    public void setPrestation(Prestation prestation) {
        if (this.prestation != null) {
            this.prestation.setDemande(null);
        }
        if (prestation != null) {
            prestation.setDemande(this);
        }
        this.prestation = prestation;
    }

    public Demande prestation(Prestation prestation) {
        this.setPrestation(prestation);
        return this;
    }

    public CodeQrService getCodeQrService() {
        return this.codeQrService;
    }

    public void setCodeQrService(CodeQrService codeQrService) {
        if (this.codeQrService != null) {
            this.codeQrService.setDemande(null);
        }
        if (codeQrService != null) {
            codeQrService.setDemande(this);
        }
        this.codeQrService = codeQrService;
    }

    public Demande codeQrService(CodeQrService codeQrService) {
        this.setCodeQrService(codeQrService);
        return this;
    }

    public Facture getFacture() {
        return this.facture;
    }

    public void setFacture(Facture facture) {
        if (this.facture != null) {
            this.facture.setDemande(null);
        }
        if (facture != null) {
            facture.setDemande(this);
        }
        this.facture = facture;
    }

    public Demande facture(Facture facture) {
        this.setFacture(facture);
        return this;
    }

    public Evaluation getEvaluation() {
        return this.evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        if (this.evaluation != null) {
            this.evaluation.setDemande(null);
        }
        if (evaluation != null) {
            evaluation.setDemande(this);
        }
        this.evaluation = evaluation;
    }

    public Demande evaluation(Evaluation evaluation) {
        this.setEvaluation(evaluation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Demande)) {
            return false;
        }
        return getId() != null && getId().equals(((Demande) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Demande{" +
            "id=" + getId() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
