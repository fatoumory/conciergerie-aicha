package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.EligibiliteServiceDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.EligibiliteService}.
 */
public interface EligibiliteServiceService {
    /**
     * Save a eligibiliteService.
     *
     * @param eligibiliteServiceDTO the entity to save.
     * @return the persisted entity.
     */
    EligibiliteServiceDTO save(EligibiliteServiceDTO eligibiliteServiceDTO);

    /**
     * Updates a eligibiliteService.
     *
     * @param eligibiliteServiceDTO the entity to update.
     * @return the persisted entity.
     */
    EligibiliteServiceDTO update(EligibiliteServiceDTO eligibiliteServiceDTO);

    /**
     * Partially updates a eligibiliteService.
     *
     * @param eligibiliteServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EligibiliteServiceDTO> partialUpdate(EligibiliteServiceDTO eligibiliteServiceDTO);

    /**
     * Get all the eligibiliteServices.
     *
     * @return the list of entities.
     */
    List<EligibiliteServiceDTO> findAll();

    /**
     * Get all the eligibiliteServices with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<EligibiliteServiceDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" eligibiliteService.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EligibiliteServiceDTO> findOne(UUID id);

    /**
     * Delete the "id" eligibiliteService.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
