package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.TypeDemandeDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.TypeDemande}.
 */
public interface TypeDemandeService {
    /**
     * Save a typeDemande.
     *
     * @param typeDemandeDTO the entity to save.
     * @return the persisted entity.
     */
    TypeDemandeDTO save(TypeDemandeDTO typeDemandeDTO);

    /**
     * Updates a typeDemande.
     *
     * @param typeDemandeDTO the entity to update.
     * @return the persisted entity.
     */
    TypeDemandeDTO update(TypeDemandeDTO typeDemandeDTO);

    /**
     * Partially updates a typeDemande.
     *
     * @param typeDemandeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeDemandeDTO> partialUpdate(TypeDemandeDTO typeDemandeDTO);

    /**
     * Get all the typeDemandes.
     *
     * @return the list of entities.
     */
    List<TypeDemandeDTO> findAll();

    /**
     * Get the "id" typeDemande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeDemandeDTO> findOne(UUID id);

    /**
     * Delete the "id" typeDemande.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
