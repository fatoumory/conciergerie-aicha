package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import sn.orange.conciergerie.domain.enumeration.PeriodeQuota;
import sn.orange.conciergerie.domain.enumeration.UniteQuota;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.QuotaService} entity.
 */
@Schema(description = "Limite d'utilisation associée à une éligibilité.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotaServiceDTO implements Serializable {

    private UUID id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal limite;

    @NotNull
    private UniteQuota unite;

    @NotNull
    private PeriodeQuota periode;

    @NotNull
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @NotNull
    private EligibiliteServiceDTO eligibiliteService;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public UniteQuota getUnite() {
        return unite;
    }

    public void setUnite(UniteQuota unite) {
        this.unite = unite;
    }

    public PeriodeQuota getPeriode() {
        return periode;
    }

    public void setPeriode(PeriodeQuota periode) {
        this.periode = periode;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public EligibiliteServiceDTO getEligibiliteService() {
        return eligibiliteService;
    }

    public void setEligibiliteService(EligibiliteServiceDTO eligibiliteService) {
        this.eligibiliteService = eligibiliteService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotaServiceDTO)) {
            return false;
        }

        QuotaServiceDTO quotaServiceDTO = (QuotaServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quotaServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotaServiceDTO{" +
            "id='" + getId() + "'" +
            ", limite=" + getLimite() +
            ", unite='" + getUnite() + "'" +
            ", periode='" + getPeriode() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", eligibiliteService=" + getEligibiliteService() +
            "}";
    }
}
