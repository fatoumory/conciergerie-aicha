package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.CodePromo;
import sn.orange.conciergerie.service.dto.CodePromoDTO;

/**
 * Mapper for the entity {@link CodePromo} and its DTO {@link CodePromoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CodePromoMapper extends EntityMapper<CodePromoDTO, CodePromo> {}
