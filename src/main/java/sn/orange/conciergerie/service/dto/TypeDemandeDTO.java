package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.TypeDemande} entity.
 */
@Schema(description = "Catégorie d'une demande.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeDemandeDTO implements Serializable {

    private UUID id;

    @NotNull
    private String code;

    @NotNull
    private String libelle;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeDemandeDTO)) {
            return false;
        }

        TypeDemandeDTO typeDemandeDTO = (TypeDemandeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeDemandeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeDemandeDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
