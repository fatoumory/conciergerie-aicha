package sn.orange.conciergerie.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.orange.conciergerie.service.dto.EvaluationDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.Evaluation}.
 */
public interface EvaluationService {
    /**
     * Save a evaluation.
     *
     * @param evaluationDTO the entity to save.
     * @return the persisted entity.
     */
    EvaluationDTO save(EvaluationDTO evaluationDTO);

    /**
     * Updates a evaluation.
     *
     * @param evaluationDTO the entity to update.
     * @return the persisted entity.
     */
    EvaluationDTO update(EvaluationDTO evaluationDTO);

    /**
     * Partially updates a evaluation.
     *
     * @param evaluationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EvaluationDTO> partialUpdate(EvaluationDTO evaluationDTO);

    /**
     * Get all the evaluations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EvaluationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" evaluation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EvaluationDTO> findOne(UUID id);

    /**
     * Delete the "id" evaluation.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
