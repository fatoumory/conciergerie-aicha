package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.CompteStockDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.CompteStock}.
 */
public interface CompteStockService {
    /**
     * Save a compteStock.
     *
     * @param compteStockDTO the entity to save.
     * @return the persisted entity.
     */
    CompteStockDTO save(CompteStockDTO compteStockDTO);

    /**
     * Updates a compteStock.
     *
     * @param compteStockDTO the entity to update.
     * @return the persisted entity.
     */
    CompteStockDTO update(CompteStockDTO compteStockDTO);

    /**
     * Partially updates a compteStock.
     *
     * @param compteStockDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompteStockDTO> partialUpdate(CompteStockDTO compteStockDTO);

    /**
     * Get all the compteStocks.
     *
     * @return the list of entities.
     */
    List<CompteStockDTO> findAll();

    /**
     * Get all the compteStocks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<CompteStockDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" compteStock.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompteStockDTO> findOne(UUID id);

    /**
     * Delete the "id" compteStock.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
