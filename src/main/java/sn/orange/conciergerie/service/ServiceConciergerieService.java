package sn.orange.conciergerie.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import sn.orange.conciergerie.service.dto.ServiceConciergerieDTO;

/**
 * Service Interface for managing {@link sn.orange.conciergerie.domain.ServiceConciergerie}.
 */
public interface ServiceConciergerieService {
    /**
     * Save a serviceConciergerie.
     *
     * @param serviceConciergerieDTO the entity to save.
     * @return the persisted entity.
     */
    ServiceConciergerieDTO save(ServiceConciergerieDTO serviceConciergerieDTO);

    /**
     * Updates a serviceConciergerie.
     *
     * @param serviceConciergerieDTO the entity to update.
     * @return the persisted entity.
     */
    ServiceConciergerieDTO update(ServiceConciergerieDTO serviceConciergerieDTO);

    /**
     * Partially updates a serviceConciergerie.
     *
     * @param serviceConciergerieDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServiceConciergerieDTO> partialUpdate(ServiceConciergerieDTO serviceConciergerieDTO);

    /**
     * Get all the serviceConciergeries.
     *
     * @return the list of entities.
     */
    List<ServiceConciergerieDTO> findAll();

    /**
     * Get all the ServiceConciergerieDTO where CompteStock is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ServiceConciergerieDTO> findAllWhereCompteStockIsNull();

    /**
     * Get all the serviceConciergeries with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<ServiceConciergerieDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" serviceConciergerie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceConciergerieDTO> findOne(UUID id);

    /**
     * Delete the "id" serviceConciergerie.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
