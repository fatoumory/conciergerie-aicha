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
import sn.orange.conciergerie.domain.enumeration.StatutCodeQr;

/**
 * QR Code matérialisant le droit d'accès à un service.
 */
@Entity
@Table(name = "code_qr_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CodeQrService implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Lob
    @Column(name = "qr_code", nullable = false)
    private String qrCode;

    @NotNull
    @Column(name = "date_generation", nullable = false)
    private Instant dateGeneration;

    @Column(name = "date_expiration")
    private Instant dateExpiration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutCodeQr statut;

    @JsonIgnoreProperties(
        value = { "client", "service", "typeDemande", "statut", "codePromo", "prestation", "codeQrService", "facture", "evaluation" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Demande demande;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public CodeQrService id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public CodeQrService code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQrCode() {
        return this.qrCode;
    }

    public CodeQrService qrCode(String qrCode) {
        this.setQrCode(qrCode);
        return this;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Instant getDateGeneration() {
        return this.dateGeneration;
    }

    public CodeQrService dateGeneration(Instant dateGeneration) {
        this.setDateGeneration(dateGeneration);
        return this;
    }

    public void setDateGeneration(Instant dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public Instant getDateExpiration() {
        return this.dateExpiration;
    }

    public CodeQrService dateExpiration(Instant dateExpiration) {
        this.setDateExpiration(dateExpiration);
        return this;
    }

    public void setDateExpiration(Instant dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public StatutCodeQr getStatut() {
        return this.statut;
    }

    public CodeQrService statut(StatutCodeQr statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutCodeQr statut) {
        this.statut = statut;
    }

    public Demande getDemande() {
        return this.demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public CodeQrService demande(Demande demande) {
        this.setDemande(demande);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodeQrService)) {
            return false;
        }
        return getId() != null && getId().equals(((CodeQrService) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodeQrService{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", qrCode='" + getQrCode() + "'" +
            ", dateGeneration='" + getDateGeneration() + "'" +
            ", dateExpiration='" + getDateExpiration() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
