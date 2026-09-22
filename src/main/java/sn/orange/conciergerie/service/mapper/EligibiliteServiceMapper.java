package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.EligibiliteService;
import sn.orange.conciergerie.domain.SegmentClient;
import sn.orange.conciergerie.domain.ServiceConciergerie;
import sn.orange.conciergerie.domain.TypeClient;
import sn.orange.conciergerie.service.dto.EligibiliteServiceDTO;
import sn.orange.conciergerie.service.dto.SegmentClientDTO;
import sn.orange.conciergerie.service.dto.ServiceConciergerieDTO;
import sn.orange.conciergerie.service.dto.TypeClientDTO;

/**
 * Mapper for the entity {@link EligibiliteService} and its DTO {@link EligibiliteServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface EligibiliteServiceMapper extends EntityMapper<EligibiliteServiceDTO, EligibiliteService> {
    @Mapping(target = "service", source = "service", qualifiedByName = "serviceConciergerieLibelle")
    @Mapping(target = "segmentClient", source = "segmentClient", qualifiedByName = "segmentClientLibelle")
    @Mapping(target = "typeClient", source = "typeClient", qualifiedByName = "typeClientLibelle")
    EligibiliteServiceDTO toDto(EligibiliteService s);

    @Named("serviceConciergerieLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    ServiceConciergerieDTO toDtoServiceConciergerieLibelle(ServiceConciergerie serviceConciergerie);

    @Named("segmentClientLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    SegmentClientDTO toDtoSegmentClientLibelle(SegmentClient segmentClient);

    @Named("typeClientLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    TypeClientDTO toDtoTypeClientLibelle(TypeClient typeClient);
}
