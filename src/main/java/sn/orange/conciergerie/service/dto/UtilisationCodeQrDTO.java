package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.UtilisationCodeQr} entity.
 */
@Schema(description = "Scan / utilisation du QR par un partenaire.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisationCodeQrDTO implements Serializable {

    private UUID id;

    @NotNull
    private Instant dateUtilisation;

    @NotNull
    private CodeQrServiceDTO codeQrService;

    @NotNull
    private PartenaireDTO partenaire;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getDateUtilisation() {
        return dateUtilisation;
    }

    public void setDateUtilisation(Instant dateUtilisation) {
        this.dateUtilisation = dateUtilisation;
    }

    public CodeQrServiceDTO getCodeQrService() {
        return codeQrService;
    }

    public void setCodeQrService(CodeQrServiceDTO codeQrService) {
        this.codeQrService = codeQrService;
    }

    public PartenaireDTO getPartenaire() {
        return partenaire;
    }

    public void setPartenaire(PartenaireDTO partenaire) {
        this.partenaire = partenaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilisationCodeQrDTO)) {
            return false;
        }

        UtilisationCodeQrDTO utilisationCodeQrDTO = (UtilisationCodeQrDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, utilisationCodeQrDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisationCodeQrDTO{" +
            "id='" + getId() + "'" +
            ", dateUtilisation='" + getDateUtilisation() + "'" +
            ", codeQrService=" + getCodeQrService() +
            ", partenaire=" + getPartenaire() +
            "}";
    }
}
