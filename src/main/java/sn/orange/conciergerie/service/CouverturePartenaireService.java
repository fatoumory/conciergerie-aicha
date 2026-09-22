package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.CouverturePartenaireDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.CouverturePartenaire}.
 */
public interface CouverturePartenaireService {
    /**
     * Save a couverturePartenaire.
     *
     * @param couverturePartenaireDTO the entity to save.
     * @return the persisted entity.
     */
    CouverturePartenaireDTO save(CouverturePartenaireDTO couverturePartenaireDTO);

    /**
     * Updates a couverturePartenaire.
     *
     * @param couverturePartenaireDTO the entity to update.
     * @return the persisted entity.
     */
    CouverturePartenaireDTO update(CouverturePartenaireDTO couverturePartenaireDTO);

    /**
     * Partially updates a couverturePartenaire.
     *
     * @param couverturePartenaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CouverturePartenaireDTO> partialUpdate(CouverturePartenaireDTO couverturePartenaireDTO);

    /**
     * Get all the couverturePartenaires.
     *
     * @return the list of entities.
     */
    List<CouverturePartenaireDTO> findAll();

    /**
     * Get all the couverturePartenaires with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<CouverturePartenaireDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" couverturePartenaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CouverturePartenaireDTO> findOne(UUID id);

    /**
     * Delete the "id" couverturePartenaire.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
