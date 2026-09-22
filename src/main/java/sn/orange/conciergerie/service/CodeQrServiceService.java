package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.CodeQrServiceDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.CodeQrService}.
 */
public interface CodeQrServiceService {
    /**
     * Save a codeQrService.
     *
     * @param codeQrServiceDTO the entity to save.
     * @return the persisted entity.
     */
    CodeQrServiceDTO save(CodeQrServiceDTO codeQrServiceDTO);

    /**
     * Updates a codeQrService.
     *
     * @param codeQrServiceDTO the entity to update.
     * @return the persisted entity.
     */
    CodeQrServiceDTO update(CodeQrServiceDTO codeQrServiceDTO);

    /**
     * Partially updates a codeQrService.
     *
     * @param codeQrServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CodeQrServiceDTO> partialUpdate(CodeQrServiceDTO codeQrServiceDTO);

    /**
     * Get all the codeQrServices.
     *
     * @return the list of entities.
     */
    List<CodeQrServiceDTO> findAll();

    /**
     * Get the "id" codeQrService.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CodeQrServiceDTO> findOne(UUID id);

    /**
     * Delete the "id" codeQrService.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
