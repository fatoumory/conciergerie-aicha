package sn.orange.conciergerie.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.orange.conciergerie.service.dto.UtilisationCodeQrDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.UtilisationCodeQr}.
 */
public interface UtilisationCodeQrService {
    /**
     * Save a utilisationCodeQr.
     *
     * @param utilisationCodeQrDTO the entity to save.
     * @return the persisted entity.
     */
    UtilisationCodeQrDTO save(UtilisationCodeQrDTO utilisationCodeQrDTO);

    /**
     * Updates a utilisationCodeQr.
     *
     * @param utilisationCodeQrDTO the entity to update.
     * @return the persisted entity.
     */
    UtilisationCodeQrDTO update(UtilisationCodeQrDTO utilisationCodeQrDTO);

    /**
     * Partially updates a utilisationCodeQr.
     *
     * @param utilisationCodeQrDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UtilisationCodeQrDTO> partialUpdate(UtilisationCodeQrDTO utilisationCodeQrDTO);

    /**
     * Get all the utilisationCodeQrs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UtilisationCodeQrDTO> findAll(Pageable pageable);

    /**
     * Get all the utilisationCodeQrs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UtilisationCodeQrDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" utilisationCodeQr.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UtilisationCodeQrDTO> findOne(UUID id);

    /**
     * Delete the "id" utilisationCodeQr.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
