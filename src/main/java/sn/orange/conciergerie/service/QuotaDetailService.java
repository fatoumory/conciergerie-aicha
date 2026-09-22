package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.QuotaDetailDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.QuotaDetail}.
 */
public interface QuotaDetailService {
    /**
     * Save a quotaDetail.
     *
     * @param quotaDetailDTO the entity to save.
     * @return the persisted entity.
     */
    QuotaDetailDTO save(QuotaDetailDTO quotaDetailDTO);

    /**
     * Updates a quotaDetail.
     *
     * @param quotaDetailDTO the entity to update.
     * @return the persisted entity.
     */
    QuotaDetailDTO update(QuotaDetailDTO quotaDetailDTO);

    /**
     * Partially updates a quotaDetail.
     *
     * @param quotaDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuotaDetailDTO> partialUpdate(QuotaDetailDTO quotaDetailDTO);

    /**
     * Get all the quotaDetails.
     *
     * @return the list of entities.
     */
    List<QuotaDetailDTO> findAll();

    /**
     * Get all the quotaDetails with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<QuotaDetailDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" quotaDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuotaDetailDTO> findOne(UUID id);

    /**
     * Delete the "id" quotaDetail.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
