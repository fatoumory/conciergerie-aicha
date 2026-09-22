package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.TypeServiceDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.TypeService}.
 */
public interface TypeServiceService {
    /**
     * Save a typeService.
     *
     * @param typeServiceDTO the entity to save.
     * @return the persisted entity.
     */
    TypeServiceDTO save(TypeServiceDTO typeServiceDTO);

    /**
     * Updates a typeService.
     *
     * @param typeServiceDTO the entity to update.
     * @return the persisted entity.
     */
    TypeServiceDTO update(TypeServiceDTO typeServiceDTO);

    /**
     * Partially updates a typeService.
     *
     * @param typeServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeServiceDTO> partialUpdate(TypeServiceDTO typeServiceDTO);

    /**
     * Get all the typeServices.
     *
     * @return the list of entities.
     */
    List<TypeServiceDTO> findAll();

    /**
     * Get the "id" typeService.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeServiceDTO> findOne(UUID id);

    /**
     * Delete the "id" typeService.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
