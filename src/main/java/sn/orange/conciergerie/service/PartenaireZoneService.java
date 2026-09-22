package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.PartenaireZoneDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.PartenaireZone}.
 */
public interface PartenaireZoneService {
    /**
     * Save a partenaireZone.
     *
     * @param partenaireZoneDTO the entity to save.
     * @return the persisted entity.
     */
    PartenaireZoneDTO save(PartenaireZoneDTO partenaireZoneDTO);

    /**
     * Updates a partenaireZone.
     *
     * @param partenaireZoneDTO the entity to update.
     * @return the persisted entity.
     */
    PartenaireZoneDTO update(PartenaireZoneDTO partenaireZoneDTO);

    /**
     * Partially updates a partenaireZone.
     *
     * @param partenaireZoneDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PartenaireZoneDTO> partialUpdate(PartenaireZoneDTO partenaireZoneDTO);

    /**
     * Get all the partenaireZones.
     *
     * @return the list of entities.
     */
    List<PartenaireZoneDTO> findAll();

    /**
     * Get all the partenaireZones with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<PartenaireZoneDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" partenaireZone.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PartenaireZoneDTO> findOne(UUID id);

    /**
     * Delete the "id" partenaireZone.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
