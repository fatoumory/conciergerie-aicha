package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.ZoneDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.Zone}.
 */
public interface ZoneService {
    /**
     * Save a zone.
     *
     * @param zoneDTO the entity to save.
     * @return the persisted entity.
     */
    ZoneDTO save(ZoneDTO zoneDTO);

    /**
     * Updates a zone.
     *
     * @param zoneDTO the entity to update.
     * @return the persisted entity.
     */
    ZoneDTO update(ZoneDTO zoneDTO);

    /**
     * Partially updates a zone.
     *
     * @param zoneDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ZoneDTO> partialUpdate(ZoneDTO zoneDTO);

    /**
     * Get all the zones.
     *
     * @return the list of entities.
     */
    List<ZoneDTO> findAll();

    /**
     * Get the "id" zone.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ZoneDTO> findOne(UUID id);

    /**
     * Delete the "id" zone.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
