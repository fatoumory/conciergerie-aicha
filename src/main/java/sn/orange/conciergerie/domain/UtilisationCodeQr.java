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
 * Scan / utilisation du QR par un partenaire.
 */
@Entity
@Table(name = "utilisation_code_qr")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisationCodeQr implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "date_utilisation", nullable = false)
    private Instant dateUtilisation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "demande" }, allowSetters = true)
    private CodeQrService codeQrService;

    @ManyToOne(optional = false)
    @NotNull
    private Partenaire partenaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public UtilisationCodeQr id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getDateUtilisation() {
        return this.dateUtilisation;
    }

    public UtilisationCodeQr dateUtilisation(Instant dateUtilisation) {
        this.setDateUtilisation(dateUtilisation);
        return this;
    }

    public void setDateUtilisation(Instant dateUtilisation) {
        this.dateUtilisation = dateUtilisation;
    }

    public CodeQrService getCodeQrService() {
        return this.codeQrService;
    }

    public void setCodeQrService(CodeQrService codeQrService) {
        this.codeQrService = codeQrService;
    }

    public UtilisationCodeQr codeQrService(CodeQrService codeQrService) {
        this.setCodeQrService(codeQrService);
        return this;
    }

    public Partenaire getPartenaire() {
        return this.partenaire;
    }

    public void setPartenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
    }

    public UtilisationCodeQr partenaire(Partenaire partenaire) {
        this.setPartenaire(partenaire);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilisationCodeQr)) {
            return false;
        }
        return getId() != null && getId().equals(((UtilisationCodeQr) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisationCodeQr{" +
            "id=" + getId() +
            ", dateUtilisation='" + getDateUtilisation() + "'" +
            "}";
    }
}
