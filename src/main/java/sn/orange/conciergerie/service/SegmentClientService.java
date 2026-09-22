package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.SegmentClientDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.SegmentClient}.
 */
public interface SegmentClientService {
    /**
     * Save a segmentClient.
     *
     * @param segmentClientDTO the entity to save.
     * @return the persisted entity.
     */
    SegmentClientDTO save(SegmentClientDTO segmentClientDTO);

    /**
     * Updates a segmentClient.
     *
     * @param segmentClientDTO the entity to update.
     * @return the persisted entity.
     */
    SegmentClientDTO update(SegmentClientDTO segmentClientDTO);

    /**
     * Partially updates a segmentClient.
     *
     * @param segmentClientDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SegmentClientDTO> partialUpdate(SegmentClientDTO segmentClientDTO);

    /**
     * Get all the segmentClients.
     *
     * @return the list of entities.
     */
    List<SegmentClientDTO> findAll();

    /**
     * Get the "id" segmentClient.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SegmentClientDTO> findOne(UUID id);

    /**
     * Delete the "id" segmentClient.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
