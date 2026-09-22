package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.PrestationDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.Prestation}.
 */
public interface PrestationService {
    /**
     * Save a prestation.
     *
     * @param prestationDTO the entity to save.
     * @return the persisted entity.
     */
    PrestationDTO save(PrestationDTO prestationDTO);

    /**
     * Updates a prestation.
     *
     * @param prestationDTO the entity to update.
     * @return the persisted entity.
     */
    PrestationDTO update(PrestationDTO prestationDTO);

    /**
     * Partially updates a prestation.
     *
     * @param prestationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrestationDTO> partialUpdate(PrestationDTO prestationDTO);

    /**
     * Get all the prestations.
     *
     * @return the list of entities.
     */
    List<PrestationDTO> findAll();

    /**
     * Get all the prestations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<PrestationDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" prestation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PrestationDTO> findOne(UUID id);

    /**
     * Delete the "id" prestation.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
