package sn.orange.conciergerie.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import sn.orange.conciergerie.domain.AffectationDemande;
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.service.dto.AffectationDemandeDTO;
import sn.orange.conciergerie.service.dto.DemandeDTO;
import sn.orange.conciergerie.service.dto.PartenaireDTO;

/**
 * Mapper for the entity {@link AffectationDemande} and its DTO {@link AffectationDemandeDTO}.
 */
@Mapper(componentModel = "spring")
public interface AffectationDemandeMapper extends EntityMapper<AffectationDemandeDTO, AffectationDemande> {
    @Mapping(target = "demande", source = "demande", qualifiedByName = "demandeId")
    @Mapping(target = "partenaire", source = "partenaire", qualifiedByName = "partenaireLibelle")
    AffectationDemandeDTO toDto(AffectationDemande s);

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
