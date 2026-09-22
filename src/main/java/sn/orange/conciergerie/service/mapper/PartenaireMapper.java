package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.service.dto.PartenaireDTO;

/**
 * Mapper for the entity {@link Partenaire} and its DTO {@link PartenaireDTO}.
 */
@Mapper(componentModel = "spring")
public interface PartenaireMapper extends EntityMapper<PartenaireDTO, Partenaire> {}
