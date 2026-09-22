package sn.orange.conciergerie.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.orange.conciergerie.service.dto.ConsommationQuotaDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.ConsommationQuota}.
 */
public interface ConsommationQuotaService {
    /**
     * Save a consommationQuota.
     *
     * @param consommationQuotaDTO the entity to save.
     * @return the persisted entity.
     */
    ConsommationQuotaDTO save(ConsommationQuotaDTO consommationQuotaDTO);

    /**
     * Updates a consommationQuota.
     *
     * @param consommationQuotaDTO the entity to update.
     * @return the persisted entity.
     */
    ConsommationQuotaDTO update(ConsommationQuotaDTO consommationQuotaDTO);

    /**
     * Partially updates a consommationQuota.
     *
     * @param consommationQuotaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConsommationQuotaDTO> partialUpdate(ConsommationQuotaDTO consommationQuotaDTO);

    /**
     * Get all the consommationQuotas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConsommationQuotaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" consommationQuota.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConsommationQuotaDTO> findOne(UUID id);

    /**
     * Delete the "id" consommationQuota.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
