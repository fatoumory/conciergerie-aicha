package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.TypeService;
import sn.orange.conciergerie.service.dto.TypeServiceDTO;

/**
 * Mapper for the entity {@link TypeService} and its DTO {@link TypeServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeServiceMapper extends EntityMapper<TypeServiceDTO, TypeService> {}
