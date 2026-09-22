package sn.orange.conciergerie.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.orange.conciergerie.service.dto.JournalAuditDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.JournalAudit}.
 */
public interface JournalAuditService {
    /**
     * Save a journalAudit.
     *
     * @param journalAuditDTO the entity to save.
     * @return the persisted entity.
     */
    JournalAuditDTO save(JournalAuditDTO journalAuditDTO);

    /**
     * Updates a journalAudit.
     *
     * @param journalAuditDTO the entity to update.
     * @return the persisted entity.
     */
    JournalAuditDTO update(JournalAuditDTO journalAuditDTO);

    /**
     * Partially updates a journalAudit.
     *
     * @param journalAuditDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<JournalAuditDTO> partialUpdate(JournalAuditDTO journalAuditDTO);

    /**
     * Get all the journalAudits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<JournalAuditDTO> findAll(Pageable pageable);

    /**
     * Get the "id" journalAudit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JournalAuditDTO> findOne(UUID id);

    /**
     * Delete the "id" journalAudit.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
