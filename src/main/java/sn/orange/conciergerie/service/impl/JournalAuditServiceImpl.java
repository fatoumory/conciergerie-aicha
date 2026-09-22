package sn.orange.conciergerie.service.impl;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.domain.JournalAudit;
import sn.orange.conciergerie.repository.JournalAuditRepository;
import sn.orange.conciergerie.service.JournalAuditService;
import sn.orange.conciergerie.service.dto.JournalAuditDTO;
import sn.orange.conciergerie.service.mapper.JournalAuditMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.JournalAudit}.
 */
@Service
@Transactional
public class JournalAuditServiceImpl implements JournalAuditService {

    private static final Logger LOG = LoggerFactory.getLogger(JournalAuditServiceImpl.class);

    private final JournalAuditRepository journalAuditRepository;

    private final JournalAuditMapper journalAuditMapper;

    public JournalAuditServiceImpl(JournalAuditRepository journalAuditRepository, JournalAuditMapper journalAuditMapper) {
        this.journalAuditRepository = journalAuditRepository;
        this.journalAuditMapper = journalAuditMapper;
    }

    @Override
    public JournalAuditDTO save(JournalAuditDTO journalAuditDTO) {
        LOG.debug("Request to save JournalAudit : {}", journalAuditDTO);
        JournalAudit journalAudit = journalAuditMapper.toEntity(journalAuditDTO);
        journalAudit = journalAuditRepository.save(journalAudit);
        return journalAuditMapper.toDto(journalAudit);
    }

    @Override
    public JournalAuditDTO update(JournalAuditDTO journalAuditDTO) {
        LOG.debug("Request to update JournalAudit : {}", journalAuditDTO);
        JournalAudit journalAudit = journalAuditMapper.toEntity(journalAuditDTO);
        journalAudit = journalAuditRepository.save(journalAudit);
        return journalAuditMapper.toDto(journalAudit);
    }

    @Override
    public Optional<JournalAuditDTO> partialUpdate(JournalAuditDTO journalAuditDTO) {
        LOG.debug("Request to partially update JournalAudit : {}", journalAuditDTO);

        return journalAuditRepository
            .findById(journalAuditDTO.getId())
            .map(existingJournalAudit -> {
                journalAuditMapper.partialUpdate(existingJournalAudit, journalAuditDTO);

                return existingJournalAudit;
            })
            .map(journalAuditRepository::save)
            .map(journalAuditMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JournalAuditDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all JournalAudits");
        return journalAuditRepository.findAll(pageable).map(journalAuditMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JournalAuditDTO> findOne(UUID id) {
        LOG.debug("Request to get JournalAudit : {}", id);
        return journalAuditRepository.findById(id).map(journalAuditMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete JournalAudit : {}", id);
        journalAuditRepository.deleteById(id);
    }
}
