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
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.repository.DemandeRepository;
import sn.orange.conciergerie.service.criteria.DemandeCriteria;
import sn.orange.conciergerie.service.dto.DemandeDTO;
import sn.orange.conciergerie.service.mapper.DemandeMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Demande} entities in the database.
 * The main input is a {@link DemandeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DemandeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DemandeQueryService extends QueryService<Demande> {

    private static final Logger LOG = LoggerFactory.getLogger(DemandeQueryService.class);

    private final DemandeRepository demandeRepository;

    private final DemandeMapper demandeMapper;

    public DemandeQueryService(DemandeRepository demandeRepository, DemandeMapper demandeMapper) {
        this.demandeRepository = demandeRepository;
        this.demandeMapper = demandeMapper;
    }

    /**
     * Return a {@link Page} of {@link DemandeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DemandeDTO> findByCriteria(DemandeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Demande> specification = createSpecification(criteria);
        return demandeRepository.findAll(specification, page).map(demandeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DemandeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Demande> specification = createSpecification(criteria);
        return demandeRepository.count(specification);
    }

    /**
     * Function to convert {@link DemandeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Demande> createSpecification(DemandeCriteria criteria) {
        Specification<Demande> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Demande_.client, JoinType.LEFT);
                root.fetch(Demande_.service, JoinType.LEFT);
                root.fetch(Demande_.typeDemande, JoinType.LEFT);
                root.fetch(Demande_.statut, JoinType.LEFT);
                root.fetch(Demande_.codePromo, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildSpecification(criteria.getId(), Demande_.id),
                    buildRangeSpecification(criteria.getDateCreation(), Demande_.dateCreation),
                    buildStringSpecification(criteria.getDescription(), Demande_.description),
                    buildSpecification(criteria.getClientId(), root -> root.join(Demande_.client, JoinType.LEFT).get(Client_.id)),
                    buildSpecification(criteria.getServiceId(), root ->
                        root.join(Demande_.service, JoinType.LEFT).get(ServiceConciergerie_.id)
                    ),
                    buildSpecification(criteria.getTypeDemandeId(), root ->
                        root.join(Demande_.typeDemande, JoinType.LEFT).get(TypeDemande_.id)
                    ),
                    buildSpecification(criteria.getStatutId(), root -> root.join(Demande_.statut, JoinType.LEFT).get(StatutDemande_.id)),
                    buildSpecification(criteria.getCodePromoId(), root -> root.join(Demande_.codePromo, JoinType.LEFT).get(CodePromo_.id)),
                    buildSpecification(criteria.getPrestationId(), root ->
                        root.join(Demande_.prestation, JoinType.LEFT).get(Prestation_.id)
                    ),
                    buildSpecification(criteria.getCodeQrServiceId(), root ->
                        root.join(Demande_.codeQrService, JoinType.LEFT).get(CodeQrService_.id)
                    ),
                    buildSpecification(criteria.getFactureId(), root -> root.join(Demande_.facture, JoinType.LEFT).get(Facture_.id)),
                    buildSpecification(criteria.getEvaluationId(), root ->
                        root.join(Demande_.evaluation, JoinType.LEFT).get(Evaluation_.id)
                    )
                )
            );
        }
        return specification;
    }
}
