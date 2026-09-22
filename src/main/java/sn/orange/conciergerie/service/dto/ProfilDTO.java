package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.Profil} entity.
 */
@Schema(description = "Fonction métier attribuée à un utilisateur.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfilDTO implements Serializable {

    private UUID id;

    @NotNull
    private String code;

    @NotNull
    private String libelle;

    private String description;

    private Set<UserDTO> utilisateurs = new HashSet<>();

    private Set<AuthorityDTO> roles = new HashSet<>();

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

    public Set<UserDTO> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(Set<UserDTO> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public Set<AuthorityDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<AuthorityDTO> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfilDTO)) {
            return false;
        }

        ProfilDTO profilDTO = (ProfilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfilDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", utilisateurs=" + getUtilisateurs() +
            ", roles=" + getRoles() +
            "}";
    }
}
