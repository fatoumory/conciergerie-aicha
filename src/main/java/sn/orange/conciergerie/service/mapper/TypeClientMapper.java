package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.TypeClient;
import sn.orange.conciergerie.service.dto.TypeClientDTO;

/**
 * Mapper for the entity {@link TypeClient} and its DTO {@link TypeClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeClientMapper extends EntityMapper<TypeClientDTO, TypeClient> {}
