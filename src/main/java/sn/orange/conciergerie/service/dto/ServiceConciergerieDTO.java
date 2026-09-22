package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.ServiceConciergerie} entity.
 */
@Schema(description = "Service proposé aux clients (Service dans le document).")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceConciergerieDTO implements Serializable {

    private UUID id;

    @NotNull
    private String code;

    @NotNull
    private String libelle;

    private String description;

    @NotNull
    private TypeServiceDTO typeService;

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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeServiceDTO getTypeService() {
        return typeService;
    }

    public void setTypeService(TypeServiceDTO typeService) {
        this.typeService = typeService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceConciergerieDTO)) {
            return false;
        }

        ServiceConciergerieDTO serviceConciergerieDTO = (ServiceConciergerieDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceConciergerieDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceConciergerieDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", typeService=" + getTypeService() +
            "}";
    }
}
