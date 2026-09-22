package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.Demande} entity.
 */
@Schema(description = "Demande de service formulée par un client.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DemandeDTO implements Serializable {

    private UUID id;

    @NotNull
    private Instant dateCreation;

    private String description;

    @NotNull
    private ClientDTO client;

    @NotNull
    private ServiceConciergerieDTO service;

    @NotNull
    private TypeDemandeDTO typeDemande;

    @NotNull
    private StatutDemandeDTO statut;

    private CodePromoDTO codePromo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public ServiceConciergerieDTO getService() {
        return service;
    }

    public void setService(ServiceConciergerieDTO service) {
        this.service = service;
    }

    public TypeDemandeDTO getTypeDemande() {
        return typeDemande;
    }

    public void setTypeDemande(TypeDemandeDTO typeDemande) {
        this.typeDemande = typeDemande;
    }

    public StatutDemandeDTO getStatut() {
        return statut;
    }

    public void setStatut(StatutDemandeDTO statut) {
        this.statut = statut;
    }

    public CodePromoDTO getCodePromo() {
        return codePromo;
    }

    public void setCodePromo(CodePromoDTO codePromo) {
        this.codePromo = codePromo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeDTO)) {
            return false;
        }

        DemandeDTO demandeDTO = (DemandeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, demandeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeDTO{" +
            "id='" + getId() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", description='" + getDescription() + "'" +
            ", client=" + getClient() +
            ", service=" + getService() +
            ", typeDemande=" + getTypeDemande() +
            ", statut=" + getStatut() +
            ", codePromo=" + getCodePromo() +
            "}";
    }
}
