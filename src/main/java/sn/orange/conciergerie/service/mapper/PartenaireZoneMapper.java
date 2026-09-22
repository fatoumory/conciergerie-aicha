package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.domain.PartenaireZone;
import sn.orange.conciergerie.domain.Zone;
import sn.orange.conciergerie.service.dto.PartenaireDTO;
import sn.orange.conciergerie.service.dto.PartenaireZoneDTO;
import sn.orange.conciergerie.service.dto.ZoneDTO;

/**
 * Mapper for the entity {@link PartenaireZone} and its DTO {@link PartenaireZoneDTO}.
 */
@Mapper(componentModel = "spring")
public interface PartenaireZoneMapper extends EntityMapper<PartenaireZoneDTO, PartenaireZone> {
    @Mapping(target = "partenaire", source = "partenaire", qualifiedByName = "partenaireLibelle")
    @Mapping(target = "zone", source = "zone", qualifiedByName = "zoneLibelle")
    PartenaireZoneDTO toDto(PartenaireZone s);

    @Named("partenaireLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    PartenaireDTO toDtoPartenaireLibelle(Partenaire partenaire);

    @Named("zoneLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    ZoneDTO toDtoZoneLibelle(Zone zone);
}
