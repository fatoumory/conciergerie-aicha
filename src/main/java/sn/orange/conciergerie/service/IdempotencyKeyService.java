package sn.orange.conciergerie.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.orange.conciergerie.service.dto.IdempotencyKeyDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.IdempotencyKey}.
 */
public interface IdempotencyKeyService {
    /**
     * Save a idempotencyKey.
     *
     * @param idempotencyKeyDTO the entity to save.
     * @return the persisted entity.
     */
    IdempotencyKeyDTO save(IdempotencyKeyDTO idempotencyKeyDTO);

    /**
     * Updates a idempotencyKey.
     *
     * @param idempotencyKeyDTO the entity to update.
     * @return the persisted entity.
     */
    IdempotencyKeyDTO update(IdempotencyKeyDTO idempotencyKeyDTO);

    /**
     * Partially updates a idempotencyKey.
     *
     * @param idempotencyKeyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IdempotencyKeyDTO> partialUpdate(IdempotencyKeyDTO idempotencyKeyDTO);

    /**
     * Get all the idempotencyKeys.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IdempotencyKeyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" idempotencyKey.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IdempotencyKeyDTO> findOne(UUID id);

    /**
     * Delete the "id" idempotencyKey.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
