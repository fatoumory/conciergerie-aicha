package sn.orange.conciergerie.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.domain.*; // for static metamodels
import sn.orange.conciergerie.domain.Client;
import sn.orange.conciergerie.repository.ClientRepository;
import sn.orange.conciergerie.service.criteria.ClientCriteria;
import sn.orange.conciergerie.service.dto.ClientDTO;
import sn.orange.conciergerie.service.mapper.ClientMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Client} entities in the database.
 * The main input is a {@link ClientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ClientDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClientQueryService extends QueryService<Client> {

    private static final Logger LOG = LoggerFactory.getLogger(ClientQueryService.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public ClientQueryService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    /**
     * Return a {@link Page} of {@link ClientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClientDTO> findByCriteria(ClientCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Client> specification = createSpecification(criteria);
        return clientRepository.findAll(specification, page).map(clientMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClientCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Client> specification = createSpecification(criteria);
        return clientRepository.count(specification);
    }

    /**
     * Function to convert {@link ClientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Client> createSpecification(ClientCriteria criteria) {
        Specification<Client> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Client_.user, JoinType.LEFT);
                root.fetch(Client_.typeClient, JoinType.LEFT);
                root.fetch(Client_.segmentClient, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildSpecification(criteria.getId(), Client_.id),
                    buildStringSpecification(criteria.getNumero(), Client_.numero),
                    buildStringSpecification(criteria.getPrenom(), Client_.prenom),
                    buildStringSpecification(criteria.getNom(), Client_.nom),
                    buildStringSpecification(criteria.getEmail(), Client_.email),
                    buildSpecification(criteria.getUserId(), root -> root.join(Client_.user, JoinType.LEFT).get(User_.id)),
                    buildSpecification(criteria.getTypeClientId(), root ->
                        root.join(Client_.typeClient, JoinType.LEFT).get(TypeClient_.id)
                    ),
                    buildSpecification(criteria.getSegmentClientId(), root ->
                        root.join(Client_.segmentClient, JoinType.LEFT).get(SegmentClient_.id)
                    )
                )
            );
        }
        return specification;
    }
}
