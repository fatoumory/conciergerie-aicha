package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.IdempotencyKey;
import sn.orange.conciergerie.service.dto.IdempotencyKeyDTO;

/**
 * Mapper for the entity {@link IdempotencyKey} and its DTO {@link IdempotencyKeyDTO}.
 */
@Mapper(componentModel = "spring")
public interface IdempotencyKeyMapper extends EntityMapper<IdempotencyKeyDTO, IdempotencyKey> {}
