package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.SegmentClient;
import sn.orange.conciergerie.service.dto.SegmentClientDTO;

/**
 * Mapper for the entity {@link SegmentClient} and its DTO {@link SegmentClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface SegmentClientMapper extends EntityMapper<SegmentClientDTO, SegmentClient> {}
