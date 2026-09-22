package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.CompteStock;
import sn.orange.conciergerie.domain.ServiceConciergerie;
import sn.orange.conciergerie.service.dto.CompteStockDTO;
import sn.orange.conciergerie.service.dto.ServiceConciergerieDTO;

/**
 * Mapper for the entity {@link CompteStock} and its DTO {@link CompteStockDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompteStockMapper extends EntityMapper<CompteStockDTO, CompteStock> {
    @Mapping(target = "service", source = "service", qualifiedByName = "serviceConciergerieLibelle")
    CompteStockDTO toDto(CompteStock s);

    @Named("serviceConciergerieLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    ServiceConciergerieDTO toDtoServiceConciergerieLibelle(ServiceConciergerie serviceConciergerie);
}
