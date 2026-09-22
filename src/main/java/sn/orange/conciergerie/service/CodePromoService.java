package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.CodePromoDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.CodePromo}.
 */
public interface CodePromoService {
    /**
     * Save a codePromo.
     *
     * @param codePromoDTO the entity to save.
     * @return the persisted entity.
     */
    CodePromoDTO save(CodePromoDTO codePromoDTO);

    /**
     * Updates a codePromo.
     *
     * @param codePromoDTO the entity to update.
     * @return the persisted entity.
     */
    CodePromoDTO update(CodePromoDTO codePromoDTO);

    /**
     * Partially updates a codePromo.
     *
     * @param codePromoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CodePromoDTO> partialUpdate(CodePromoDTO codePromoDTO);

    /**
     * Get all the codePromos.
     *
     * @return the list of entities.
     */
    List<CodePromoDTO> findAll();

    /**
     * Get the "id" codePromo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CodePromoDTO> findOne(UUID id);

    /**
     * Delete the "id" codePromo.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
