package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import sn.orange.conciergerie.domain.enumeration.StatutCodeQr;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.CodeQrService} entity.
 */
@Schema(description = "QR Code matérialisant le droit d'accès à un service.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CodeQrServiceDTO implements Serializable {

    private UUID id;

    @NotNull
    private String code;

    @Lob
    private String qrCode;

    @NotNull
    private Instant dateGeneration;

    private Instant dateExpiration;

    @NotNull
    private StatutCodeQr statut;

    @NotNull
    private DemandeDTO demande;

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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Instant getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(Instant dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public Instant getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Instant dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public StatutCodeQr getStatut() {
        return statut;
    }

    public void setStatut(StatutCodeQr statut) {
        this.statut = statut;
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
        if (!(o instanceof CodeQrServiceDTO)) {
            return false;
        }

        CodeQrServiceDTO codeQrServiceDTO = (CodeQrServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, codeQrServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodeQrServiceDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", qrCode='" + getQrCode() + "'" +
            ", dateGeneration='" + getDateGeneration() + "'" +
            ", dateExpiration='" + getDateExpiration() + "'" +
            ", statut='" + getStatut() + "'" +
            ", demande=" + getDemande() +
            "}";
    }
}
