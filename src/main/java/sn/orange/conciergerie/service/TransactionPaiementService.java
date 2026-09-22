package sn.orange.conciergerie.service;

import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.TransactionPaiementDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.TransactionPaiement}.
 */
public interface TransactionPaiementService {
    /**
     * Save a transactionPaiement.
     *
     * @param transactionPaiementDTO the entity to save.
     * @return the persisted entity.
     */
    TransactionPaiementDTO save(TransactionPaiementDTO transactionPaiementDTO);

    /**
     * Updates a transactionPaiement.
     *
     * @param transactionPaiementDTO the entity to update.
     * @return the persisted entity.
     */
    TransactionPaiementDTO update(TransactionPaiementDTO transactionPaiementDTO);

    /**
     * Partially updates a transactionPaiement.
     *
     * @param transactionPaiementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransactionPaiementDTO> partialUpdate(TransactionPaiementDTO transactionPaiementDTO);

    /**
     * Get the "id" transactionPaiement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionPaiementDTO> findOne(UUID id);

    /**
     * Delete the "id" transactionPaiement.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
