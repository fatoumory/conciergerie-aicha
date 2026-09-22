package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.StatutDemande;
import sn.orange.conciergerie.service.dto.StatutDemandeDTO;

/**
 * Mapper for the entity {@link StatutDemande} and its DTO {@link StatutDemandeDTO}.
 */
@Mapper(componentModel = "spring")
public interface StatutDemandeMapper extends EntityMapper<StatutDemandeDTO, StatutDemande> {}
