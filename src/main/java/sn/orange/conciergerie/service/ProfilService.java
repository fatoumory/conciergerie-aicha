package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.ProfilDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.Profil}.
 */
public interface ProfilService {
    /**
     * Save a profil.
     *
     * @param profilDTO the entity to save.
     * @return the persisted entity.
     */
    ProfilDTO save(ProfilDTO profilDTO);

    /**
     * Updates a profil.
     *
     * @param profilDTO the entity to update.
     * @return the persisted entity.
     */
    ProfilDTO update(ProfilDTO profilDTO);

    /**
     * Partially updates a profil.
     *
     * @param profilDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProfilDTO> partialUpdate(ProfilDTO profilDTO);

    /**
     * Get all the profils.
     *
     * @return the list of entities.
     */
    List<ProfilDTO> findAll();

    /**
     * Get all the profils with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<ProfilDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" profil.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProfilDTO> findOne(UUID id);

    /**
     * Delete the "id" profil.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
