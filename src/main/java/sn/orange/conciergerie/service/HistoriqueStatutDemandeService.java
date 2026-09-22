package sn.orange.conciergerie.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.orange.conciergerie.service.dto.HistoriqueStatutDemandeDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.HistoriqueStatutDemande}.
 */
public interface HistoriqueStatutDemandeService {
    /**
     * Save a historiqueStatutDemande.
     *
     * @param historiqueStatutDemandeDTO the entity to save.
     * @return the persisted entity.
     */
    HistoriqueStatutDemandeDTO save(HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO);

    /**
     * Updates a historiqueStatutDemande.
     *
     * @param historiqueStatutDemandeDTO the entity to update.
     * @return the persisted entity.
     */
    HistoriqueStatutDemandeDTO update(HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO);

    /**
     * Partially updates a historiqueStatutDemande.
     *
     * @param historiqueStatutDemandeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistoriqueStatutDemandeDTO> partialUpdate(HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO);

    /**
     * Get all the historiqueStatutDemandes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoriqueStatutDemandeDTO> findAll(Pageable pageable);

    /**
     * Get all the historiqueStatutDemandes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoriqueStatutDemandeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" historiqueStatutDemande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoriqueStatutDemandeDTO> findOne(UUID id);

    /**
     * Delete the "id" historiqueStatutDemande.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
