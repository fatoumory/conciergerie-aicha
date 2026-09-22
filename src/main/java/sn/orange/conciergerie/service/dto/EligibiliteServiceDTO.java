package sn.orange.conciergerie.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link sn.orange.conciergerie.domain.EligibiliteService} entity.
 */
@Schema(description = "Règle d'accès à un service selon le segment et le type du client.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EligibiliteServiceDTO implements Serializable {

    private UUID id;

    @NotNull
    private Boolean autorise;

    @NotNull
    private Boolean gratuit;

    @NotNull
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @NotNull
    private ServiceConciergerieDTO service;

    @NotNull
    private SegmentClientDTO segmentClient;

    @NotNull
    private TypeClientDTO typeClient;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getAutorise() {
        return autorise;
    }

    public void setAutorise(Boolean autorise) {
        this.autorise = autorise;
    }

    public Boolean getGratuit() {
        return gratuit;
    }

    public void setGratuit(Boolean gratuit) {
        this.gratuit = gratuit;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public ServiceConciergerieDTO getService() {
        return service;
    }

    public void setService(ServiceConciergerieDTO service) {
        this.service = service;
    }

    public SegmentClientDTO getSegmentClient() {
        return segmentClient;
    }

    public void setSegmentClient(SegmentClientDTO segmentClient) {
        this.segmentClient = segmentClient;
    }

    public TypeClientDTO getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(TypeClientDTO typeClient) {
        this.typeClient = typeClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EligibiliteServiceDTO)) {
            return false;
        }

        EligibiliteServiceDTO eligibiliteServiceDTO = (EligibiliteServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eligibiliteServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EligibiliteServiceDTO{" +
            "id='" + getId() + "'" +
            ", autorise='" + getAutorise() + "'" +
            ", gratuit='" + getGratuit() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", service=" + getService() +
            ", segmentClient=" + getSegmentClient() +
            ", typeClient=" + getTypeClient() +
            "}";
    }
}
