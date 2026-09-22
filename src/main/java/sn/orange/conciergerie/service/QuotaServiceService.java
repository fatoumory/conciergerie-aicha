package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.QuotaServiceDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.QuotaService}.
 */
public interface QuotaServiceService {
    /**
     * Save a quotaService.
     *
     * @param quotaServiceDTO the entity to save.
     * @return the persisted entity.
     */
    QuotaServiceDTO save(QuotaServiceDTO quotaServiceDTO);

    /**
     * Updates a quotaService.
     *
     * @param quotaServiceDTO the entity to update.
     * @return the persisted entity.
     */
    QuotaServiceDTO update(QuotaServiceDTO quotaServiceDTO);

    /**
     * Partially updates a quotaService.
     *
     * @param quotaServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuotaServiceDTO> partialUpdate(QuotaServiceDTO quotaServiceDTO);

    /**
     * Get all the quotaServices.
     *
     * @return the list of entities.
     */
    List<QuotaServiceDTO> findAll();

    /**
     * Get the "id" quotaService.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuotaServiceDTO> findOne(UUID id);

    /**
     * Delete the "id" quotaService.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
