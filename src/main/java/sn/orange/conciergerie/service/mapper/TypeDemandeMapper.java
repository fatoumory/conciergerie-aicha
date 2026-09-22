package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.TypeDemande;
import sn.orange.conciergerie.service.dto.TypeDemandeDTO;

/**
 * Mapper for the entity {@link TypeDemande} and its DTO {@link TypeDemandeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeDemandeMapper extends EntityMapper<TypeDemandeDTO, TypeDemande> {}
