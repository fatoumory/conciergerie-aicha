package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import sn.orange.conciergerie.domain.enumeration.TypeMouvementStock;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.MouvementStock} entity.
 */
@Schema(description = "Mouvement de crédit/débit du stock (TransactionAccount dans le document).")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MouvementStockDTO implements Serializable {

    private UUID id;

    @NotNull
    private TypeMouvementStock type;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal quantite;

    @NotNull
    private Instant dateTransaction;

    private String motif;

    @NotNull
    private CompteStockDTO compteStock;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TypeMouvementStock getType() {
        return type;
    }

    public void setType(TypeMouvementStock type) {
        this.type = type;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public Instant getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(Instant dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public CompteStockDTO getCompteStock() {
        return compteStock;
    }

    public void setCompteStock(CompteStockDTO compteStock) {
        this.compteStock = compteStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MouvementStockDTO)) {
            return false;
        }

        MouvementStockDTO mouvementStockDTO = (MouvementStockDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mouvementStockDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MouvementStockDTO{" +
            "id='" + getId() + "'" +
            ", type='" + getType() + "'" +
            ", quantite=" + getQuantite() +
            ", dateTransaction='" + getDateTransaction() + "'" +
            ", motif='" + getMotif() + "'" +
            ", compteStock=" + getCompteStock() +
            "}";
    }
}
