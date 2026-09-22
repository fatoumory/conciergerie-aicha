package sn.orange.conciergerie.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.domain.HistoriqueStatutDemande;
import sn.orange.conciergerie.domain.StatutDemande;
import sn.orange.conciergerie.service.dto.DemandeDTO;
import sn.orange.conciergerie.service.dto.HistoriqueStatutDemandeDTO;
import sn.orange.conciergerie.service.dto.StatutDemandeDTO;

/**
 * Mapper for the entity {@link HistoriqueStatutDemande} and its DTO {@link HistoriqueStatutDemandeDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoriqueStatutDemandeMapper extends EntityMapper<HistoriqueStatutDemandeDTO, HistoriqueStatutDemande> {
    @Mapping(target = "demande", source = "demande", qualifiedByName = "demandeId")
    @Mapping(target = "statut", source = "statut", qualifiedByName = "statutDemandeLibelle")
    HistoriqueStatutDemandeDTO toDto(HistoriqueStatutDemande s);

    @Named("demandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DemandeDTO toDtoDemandeId(Demande demande);

    @Named("statutDemandeLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    StatutDemandeDTO toDtoStatutDemandeLibelle(StatutDemande statutDemande);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
