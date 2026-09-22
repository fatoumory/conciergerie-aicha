package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.TypeClientDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.TypeClient}.
 */
public interface TypeClientService {
    /**
     * Save a typeClient.
     *
     * @param typeClientDTO the entity to save.
     * @return the persisted entity.
     */
    TypeClientDTO save(TypeClientDTO typeClientDTO);

    /**
     * Updates a typeClient.
     *
     * @param typeClientDTO the entity to update.
     * @return the persisted entity.
     */
    TypeClientDTO update(TypeClientDTO typeClientDTO);

    /**
     * Partially updates a typeClient.
     *
     * @param typeClientDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeClientDTO> partialUpdate(TypeClientDTO typeClientDTO);

    /**
     * Get all the typeClients.
     *
     * @return the list of entities.
     */
    List<TypeClientDTO> findAll();

    /**
     * Get the "id" typeClient.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeClientDTO> findOne(UUID id);

    /**
     * Delete the "id" typeClient.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
