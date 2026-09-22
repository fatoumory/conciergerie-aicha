package sn.orange.conciergerie.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.orange.conciergerie.service.dto.MouvementStockDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.MouvementStock}.
 */
public interface MouvementStockService {
    /**
     * Save a mouvementStock.
     *
     * @param mouvementStockDTO the entity to save.
     * @return the persisted entity.
     */
    MouvementStockDTO save(MouvementStockDTO mouvementStockDTO);

    /**
     * Updates a mouvementStock.
     *
     * @param mouvementStockDTO the entity to update.
     * @return the persisted entity.
     */
    MouvementStockDTO update(MouvementStockDTO mouvementStockDTO);

    /**
     * Partially updates a mouvementStock.
     *
     * @param mouvementStockDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MouvementStockDTO> partialUpdate(MouvementStockDTO mouvementStockDTO);

    /**
     * Get all the mouvementStocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MouvementStockDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mouvementStock.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MouvementStockDTO> findOne(UUID id);

    /**
     * Delete the "id" mouvementStock.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
