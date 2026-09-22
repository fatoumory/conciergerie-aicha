package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.CodePromo} entity.
 */
@Schema(description = "Code promotionnel.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CodePromoDTO implements Serializable {

    private UUID id;

    @NotNull
    private String code;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal valeur;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodePromoDTO)) {
            return false;
        }

        CodePromoDTO codePromoDTO = (CodePromoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, codePromoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodePromoDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", valeur=" + getValeur() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
