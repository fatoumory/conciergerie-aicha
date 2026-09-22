package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.AffectationDemandeDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.AffectationDemande}.
 */
public interface AffectationDemandeService {
    /**
     * Save a affectationDemande.
     *
     * @param affectationDemandeDTO the entity to save.
     * @return the persisted entity.
     */
    AffectationDemandeDTO save(AffectationDemandeDTO affectationDemandeDTO);

    /**
     * Updates a affectationDemande.
     *
     * @param affectationDemandeDTO the entity to update.
     * @return the persisted entity.
     */
    AffectationDemandeDTO update(AffectationDemandeDTO affectationDemandeDTO);

    /**
     * Partially updates a affectationDemande.
     *
     * @param affectationDemandeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AffectationDemandeDTO> partialUpdate(AffectationDemandeDTO affectationDemandeDTO);

    /**
     * Get all the affectationDemandes.
     *
     * @return the list of entities.
     */
    List<AffectationDemandeDTO> findAll();

    /**
     * Get all the affectationDemandes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<AffectationDemandeDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" affectationDemande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AffectationDemandeDTO> findOne(UUID id);

    /**
     * Delete the "id" affectationDemande.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
