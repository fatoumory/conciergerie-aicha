package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.JournalAudit;
import sn.orange.conciergerie.service.dto.JournalAuditDTO;

/**
 * Mapper for the entity {@link JournalAudit} and its DTO {@link JournalAuditDTO}.
 */
@Mapper(componentModel = "spring")
public interface JournalAuditMapper extends EntityMapper<JournalAuditDTO, JournalAudit> {}
