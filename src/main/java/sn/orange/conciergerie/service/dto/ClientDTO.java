package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.Client} entity.
 */
@Schema(description = "Bénéficiaire des services. Le numéro correspond au MSISDN.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientDTO implements Serializable {

    private UUID id;

    @NotNull
    private String numero;

    private String prenom;

    private String nom;

    private String email;

    private UserDTO user;

    @NotNull
    private TypeClientDTO typeClient;

    @NotNull
    private SegmentClientDTO segmentClient;

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public TypeClientDTO getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(TypeClientDTO typeClient) {
        this.typeClient = typeClient;
    }

    public SegmentClientDTO getSegmentClient() {
        return segmentClient;
    }

    public void setSegmentClient(SegmentClientDTO segmentClient) {
        this.segmentClient = segmentClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientDTO)) {
            return false;
        }

        ClientDTO clientDTO = (ClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientDTO{" +
            "id='" + getId() + "'" +
            ", numero='" + getNumero() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", nom='" + getNom() + "'" +
            ", email='" + getEmail() + "'" +
            ", user=" + getUser() +
            ", typeClient=" + getTypeClient() +
            ", segmentClient=" + getSegmentClient() +
            "}";
    }
}
