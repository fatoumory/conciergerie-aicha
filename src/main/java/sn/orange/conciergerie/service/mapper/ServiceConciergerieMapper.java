package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.ServiceConciergerie;
import sn.orange.conciergerie.domain.TypeService;
import sn.orange.conciergerie.service.dto.ServiceConciergerieDTO;
import sn.orange.conciergerie.service.dto.TypeServiceDTO;

/**
 * Mapper for the entity {@link ServiceConciergerie} and its DTO {@link ServiceConciergerieDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceConciergerieMapper extends EntityMapper<ServiceConciergerieDTO, ServiceConciergerie> {
    @Mapping(target = "typeService", source = "typeService", qualifiedByName = "typeServiceLibelle")
    ServiceConciergerieDTO toDto(ServiceConciergerie s);

    @Named("typeServiceLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    TypeServiceDTO toDtoTypeServiceLibelle(TypeService typeService);
}
