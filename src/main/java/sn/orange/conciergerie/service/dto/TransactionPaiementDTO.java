package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import sn.orange.conciergerie.domain.enumeration.ModePaiement;
import sn.orange.conciergerie.domain.enumeration.StatutTransaction;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.TransactionPaiement} entity.
 */
@Schema(description = "Tentative de paiement d'une demande (Transaction dans le document).")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionPaiementDTO implements Serializable {

    private UUID id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal montant;

    @NotNull
    private ModePaiement modePaiement;

    @NotNull
    private StatutTransaction statut;

    private String referenceExterne;

    @NotNull
    private Instant dateTransaction;

    @NotNull
    private DemandeDTO demande;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public StatutTransaction getStatut() {
        return statut;
    }

    public void setStatut(StatutTransaction statut) {
        this.statut = statut;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public Instant getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(Instant dateTransaction) {
        this.dateTransaction = dateTransaction;
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
        if (!(o instanceof TransactionPaiementDTO)) {
            return false;
        }

        TransactionPaiementDTO transactionPaiementDTO = (TransactionPaiementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionPaiementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionPaiementDTO{" +
            "id='" + getId() + "'" +
            ", montant=" + getMontant() +
            ", modePaiement='" + getModePaiement() + "'" +
            ", statut='" + getStatut() + "'" +
            ", referenceExterne='" + getReferenceExterne() + "'" +
            ", dateTransaction='" + getDateTransaction() + "'" +
            ", demande=" + getDemande() +
            "}";
    }
}
