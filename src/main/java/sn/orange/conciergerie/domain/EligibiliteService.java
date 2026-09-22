package sn.orange.conciergerie.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Règle d'accès à un service selon le segment et le type du client.
 */
@Entity
@Table(name = "eligibilite_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EligibiliteService implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "autorise", nullable = false)
    private Boolean autorise;

    @NotNull
    @Column(name = "gratuit", nullable = false)
    private Boolean gratuit;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "typeService", "compteStock" }, allowSetters = true)
    private ServiceConciergerie service;

    @ManyToOne(optional = false)
    @NotNull
    private SegmentClient segmentClient;

    @ManyToOne(optional = false)
    @NotNull
    private TypeClient typeClient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public EligibiliteService id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getAutorise() {
        return this.autorise;
    }

    public EligibiliteService autorise(Boolean autorise) {
        this.setAutorise(autorise);
        return this;
    }

    public void setAutorise(Boolean autorise) {
        this.autorise = autorise;
    }

    public Boolean getGratuit() {
        return this.gratuit;
    }

    public EligibiliteService gratuit(Boolean gratuit) {
        this.setGratuit(gratuit);
        return this;
    }

    public void setGratuit(Boolean gratuit) {
        this.gratuit = gratuit;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public EligibiliteService dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public EligibiliteService dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public ServiceConciergerie getService() {
        return this.service;
    }

    public void setService(ServiceConciergerie serviceConciergerie) {
        this.service = serviceConciergerie;
    }

    public EligibiliteService service(ServiceConciergerie serviceConciergerie) {
        this.setService(serviceConciergerie);
        return this;
    }

    public SegmentClient getSegmentClient() {
        return this.segmentClient;
    }

    public void setSegmentClient(SegmentClient segmentClient) {
        this.segmentClient = segmentClient;
    }

    public EligibiliteService segmentClient(SegmentClient segmentClient) {
        this.setSegmentClient(segmentClient);
        return this;
    }

    public TypeClient getTypeClient() {
        return this.typeClient;
    }

    public void setTypeClient(TypeClient typeClient) {
        this.typeClient = typeClient;
    }

    public EligibiliteService typeClient(TypeClient typeClient) {
        this.setTypeClient(typeClient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EligibiliteService)) {
            return false;
        }
        return getId() != null && getId().equals(((EligibiliteService) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EligibiliteService{" +
            "id=" + getId() +
            ", autorise='" + getAutorise() + "'" +
            ", gratuit='" + getGratuit() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
