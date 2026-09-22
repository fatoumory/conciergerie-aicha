package sn.orange.conciergerie.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.orange.conciergerie.domain.enumeration.PeriodeQuota;
import sn.orange.conciergerie.domain.enumeration.UniteQuota;

/**
 * Limite d'utilisation associée à une éligibilité.
 */
@Entity
@Table(name = "quota_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotaService implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "limite", precision = 21, scale = 2, nullable = false)
    private BigDecimal limite;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unite", nullable = false)
    private UniteQuota unite;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "periode", nullable = false)
    private PeriodeQuota periode;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "service", "segmentClient", "typeClient" }, allowSetters = true)
    private EligibiliteService eligibiliteService;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public QuotaService id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getLimite() {
        return this.limite;
    }

    public QuotaService limite(BigDecimal limite) {
        this.setLimite(limite);
        return this;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public UniteQuota getUnite() {
        return this.unite;
    }

    public QuotaService unite(UniteQuota unite) {
        this.setUnite(unite);
        return this;
    }

    public void setUnite(UniteQuota unite) {
        this.unite = unite;
    }

    public PeriodeQuota getPeriode() {
        return this.periode;
    }

    public QuotaService periode(PeriodeQuota periode) {
        this.setPeriode(periode);
        return this;
    }

    public void setPeriode(PeriodeQuota periode) {
        this.periode = periode;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public QuotaService dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public QuotaService dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public EligibiliteService getEligibiliteService() {
        return this.eligibiliteService;
    }

    public void setEligibiliteService(EligibiliteService eligibiliteService) {
        this.eligibiliteService = eligibiliteService;
    }

    public QuotaService eligibiliteService(EligibiliteService eligibiliteService) {
        this.setEligibiliteService(eligibiliteService);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotaService)) {
            return false;
        }
        return getId() != null && getId().equals(((QuotaService) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotaService{" +
            "id=" + getId() +
            ", limite=" + getLimite() +
            ", unite='" + getUnite() + "'" +
            ", periode='" + getPeriode() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
