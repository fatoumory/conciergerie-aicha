package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.StatutDemandeDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.StatutDemande}.
 */
public interface StatutDemandeService {
    /**
     * Save a statutDemande.
     *
     * @param statutDemandeDTO the entity to save.
     * @return the persisted entity.
     */
    StatutDemandeDTO save(StatutDemandeDTO statutDemandeDTO);

    /**
     * Updates a statutDemande.
     *
     * @param statutDemandeDTO the entity to update.
     * @return the persisted entity.
     */
    StatutDemandeDTO update(StatutDemandeDTO statutDemandeDTO);

    /**
     * Partially updates a statutDemande.
     *
     * @param statutDemandeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StatutDemandeDTO> partialUpdate(StatutDemandeDTO statutDemandeDTO);

    /**
     * Get all the statutDemandes.
     *
     * @return the list of entities.
     */
    List<StatutDemandeDTO> findAll();

    /**
     * Get the "id" statutDemande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StatutDemandeDTO> findOne(UUID id);

    /**
     * Delete the "id" statutDemande.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
