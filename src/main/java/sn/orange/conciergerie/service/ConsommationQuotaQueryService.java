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
import sn.orange.conciergerie.domain.ConsommationQuota;
import sn.orange.conciergerie.repository.ConsommationQuotaRepository;
import sn.orange.conciergerie.service.criteria.ConsommationQuotaCriteria;
import sn.orange.conciergerie.service.dto.ConsommationQuotaDTO;
import sn.orange.conciergerie.service.mapper.ConsommationQuotaMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ConsommationQuota} entities in the database.
 * The main input is a {@link ConsommationQuotaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ConsommationQuotaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConsommationQuotaQueryService extends QueryService<ConsommationQuota> {

    private static final Logger LOG = LoggerFactory.getLogger(ConsommationQuotaQueryService.class);

    private final ConsommationQuotaRepository consommationQuotaRepository;

    private final ConsommationQuotaMapper consommationQuotaMapper;

    public ConsommationQuotaQueryService(
        ConsommationQuotaRepository consommationQuotaRepository,
        ConsommationQuotaMapper consommationQuotaMapper
    ) {
        this.consommationQuotaRepository = consommationQuotaRepository;
        this.consommationQuotaMapper = consommationQuotaMapper;
    }

    /**
     * Return a {@link Page} of {@link ConsommationQuotaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsommationQuotaDTO> findByCriteria(ConsommationQuotaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConsommationQuota> specification = createSpecification(criteria);
        return consommationQuotaRepository.findAll(specification, page).map(consommationQuotaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConsommationQuotaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ConsommationQuota> specification = createSpecification(criteria);
        return consommationQuotaRepository.count(specification);
    }

    /**
     * Function to convert {@link ConsommationQuotaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ConsommationQuota> createSpecification(ConsommationQuotaCriteria criteria) {
        Specification<ConsommationQuota> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ConsommationQuota_.client, JoinType.LEFT);
                root.fetch(ConsommationQuota_.quotaService, JoinType.LEFT);
                root.fetch(ConsommationQuota_.quotaDetail, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildSpecification(criteria.getId(), ConsommationQuota_.id),
                    buildRangeSpecification(criteria.getQuantite(), ConsommationQuota_.quantite),
                    buildRangeSpecification(criteria.getDateConsommation(), ConsommationQuota_.dateConsommation),
                    buildSpecification(criteria.getClientId(), root -> root.join(ConsommationQuota_.client, JoinType.LEFT).get(Client_.id)),
                    buildSpecification(criteria.getQuotaServiceId(), root ->
                        root.join(ConsommationQuota_.quotaService, JoinType.LEFT).get(QuotaService_.id)
                    ),
                    buildSpecification(criteria.getQuotaDetailId(), root ->
                        root.join(ConsommationQuota_.quotaDetail, JoinType.LEFT).get(QuotaDetail_.id)
                    )
                )
            );
        }
        return specification;
    }
}
