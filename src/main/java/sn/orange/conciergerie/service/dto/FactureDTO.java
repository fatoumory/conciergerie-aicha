package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.Facture} entity.
 */
@Schema(description = "Facture générée pour une demande.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactureDTO implements Serializable {

    private UUID id;

    @NotNull
    private String numero;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal montant;

    @NotNull
    private Instant dateGeneration;

    @NotNull
    private DemandeDTO demande;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Instant getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(Instant dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public DemandeDTO getDemande() {
        return demande;
    }

    public void setDemande(DemandeDTO demande) {
        this.demande = demande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactureDTO)) {
            return false;
        }

        FactureDTO factureDTO = (FactureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, factureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactureDTO{" +
            "id='" + getId() + "'" +
            ", numero='" + getNumero() + "'" +
            ", montant=" + getMontant() +
            ", dateGeneration='" + getDateGeneration() + "'" +
            ", demande=" + getDemande() +
            "}";
    }
}
