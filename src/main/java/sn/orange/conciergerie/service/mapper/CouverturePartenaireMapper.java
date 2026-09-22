package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.CouverturePartenaire;
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.domain.ServiceConciergerie;
import sn.orange.conciergerie.service.dto.CouverturePartenaireDTO;
import sn.orange.conciergerie.service.dto.PartenaireDTO;
import sn.orange.conciergerie.service.dto.ServiceConciergerieDTO;

/**
 * Mapper for the entity {@link CouverturePartenaire} and its DTO {@link CouverturePartenaireDTO}.
 */
@Mapper(componentModel = "spring")
public interface CouverturePartenaireMapper extends EntityMapper<CouverturePartenaireDTO, CouverturePartenaire> {
    @Mapping(target = "partenaire", source = "partenaire", qualifiedByName = "partenaireLibelle")
    @Mapping(target = "service", source = "service", qualifiedByName = "serviceConciergerieLibelle")
    CouverturePartenaireDTO toDto(CouverturePartenaire s);

    @Named("partenaireLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    PartenaireDTO toDtoPartenaireLibelle(Partenaire partenaire);

    @Named("serviceConciergerieLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    ServiceConciergerieDTO toDtoServiceConciergerieLibelle(ServiceConciergerie serviceConciergerie);
}
