package sn.orange.conciergerie.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.domain.Prestation;
import sn.orange.conciergerie.service.dto.DemandeDTO;
import sn.orange.conciergerie.service.dto.PartenaireDTO;
import sn.orange.conciergerie.service.dto.PrestationDTO;

/**
 * Mapper for the entity {@link Prestation} and its DTO {@link PrestationDTO}.
 */
@Mapper(componentModel = "spring")
public interface PrestationMapper extends EntityMapper<PrestationDTO, Prestation> {
    @Mapping(target = "demande", source = "demande", qualifiedByName = "demandeId")
    @Mapping(target = "partenaire", source = "partenaire", qualifiedByName = "partenaireLibelle")
    PrestationDTO toDto(Prestation s);

    @Named("demandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DemandeDTO toDtoDemandeId(Demande demande);

    @Named("partenaireLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    PartenaireDTO toDtoPartenaireLibelle(Partenaire partenaire);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
