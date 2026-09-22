package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.orange.conciergerie.service.dto.DemandeDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.Demande}.
 */
public interface DemandeService {
    /**
     * Save a demande.
     *
     * @param demandeDTO the entity to save.
     * @return the persisted entity.
     */
    DemandeDTO save(DemandeDTO demandeDTO);

    /**
     * Updates a demande.
     *
     * @param demandeDTO the entity to update.
     * @return the persisted entity.
     */
    DemandeDTO update(DemandeDTO demandeDTO);

    /**
     * Partially updates a demande.
     *
     * @param demandeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DemandeDTO> partialUpdate(DemandeDTO demandeDTO);

    /**
     * Get all the DemandeDTO where Prestation is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<DemandeDTO> findAllWherePrestationIsNull();
    /**
     * Get all the DemandeDTO where CodeQrService is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<DemandeDTO> findAllWhereCodeQrServiceIsNull();
    /**
     * Get all the DemandeDTO where Facture is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<DemandeDTO> findAllWhereFactureIsNull();
    /**
     * Get all the DemandeDTO where Evaluation is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<DemandeDTO> findAllWhereEvaluationIsNull();

    /**
     * Get all the demandes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DemandeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" demande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DemandeDTO> findOne(UUID id);

    /**
     * Delete the "id" demande.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
