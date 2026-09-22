package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.PartenaireDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.Partenaire}.
 */
public interface PartenaireService {
    /**
     * Save a partenaire.
     *
     * @param partenaireDTO the entity to save.
     * @return the persisted entity.
     */
    PartenaireDTO save(PartenaireDTO partenaireDTO);

    /**
     * Updates a partenaire.
     *
     * @param partenaireDTO the entity to update.
     * @return the persisted entity.
     */
    PartenaireDTO update(PartenaireDTO partenaireDTO);

    /**
     * Partially updates a partenaire.
     *
     * @param partenaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PartenaireDTO> partialUpdate(PartenaireDTO partenaireDTO);

    /**
     * Get all the partenaires.
     *
     * @return the list of entities.
     */
    List<PartenaireDTO> findAll();

    /**
     * Get the "id" partenaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PartenaireDTO> findOne(UUID id);

    /**
     * Delete the "id" partenaire.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
