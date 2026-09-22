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
import sn.orange.conciergerie.domain.TransactionPaiement;
import sn.orange.conciergerie.repository.TransactionPaiementRepository;
import sn.orange.conciergerie.service.criteria.TransactionPaiementCriteria;
import sn.orange.conciergerie.service.dto.TransactionPaiementDTO;
import sn.orange.conciergerie.service.mapper.TransactionPaiementMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TransactionPaiement} entities in the database.
 * The main input is a {@link TransactionPaiementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransactionPaiementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionPaiementQueryService extends QueryService<TransactionPaiement> {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionPaiementQueryService.class);

    private final TransactionPaiementRepository transactionPaiementRepository;

    private final TransactionPaiementMapper transactionPaiementMapper;

    public TransactionPaiementQueryService(
        TransactionPaiementRepository transactionPaiementRepository,
        TransactionPaiementMapper transactionPaiementMapper
    ) {
        this.transactionPaiementRepository = transactionPaiementRepository;
        this.transactionPaiementMapper = transactionPaiementMapper;
    }

    /**
     * Return a {@link Page} of {@link TransactionPaiementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionPaiementDTO> findByCriteria(TransactionPaiementCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransactionPaiement> specification = createSpecification(criteria);
        return transactionPaiementRepository.findAll(specification, page).map(transactionPaiementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionPaiementCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TransactionPaiement> specification = createSpecification(criteria);
        return transactionPaiementRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionPaiementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionPaiement> createSpecification(TransactionPaiementCriteria criteria) {
        Specification<TransactionPaiement> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(TransactionPaiement_.demande, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildSpecification(criteria.getId(), TransactionPaiement_.id),
                    buildRangeSpecification(criteria.getMontant(), TransactionPaiement_.montant),
                    buildSpecification(criteria.getModePaiement(), TransactionPaiement_.modePaiement),
                    buildSpecification(criteria.getStatut(), TransactionPaiement_.statut),
                    buildStringSpecification(criteria.getReferenceExterne(), TransactionPaiement_.referenceExterne),
                    buildRangeSpecification(criteria.getDateTransaction(), TransactionPaiement_.dateTransaction),
                    buildSpecification(criteria.getDemandeId(), root ->
                        root.join(TransactionPaiement_.demande, JoinType.LEFT).get(Demande_.id)
                    )
                )
            );
        }
        return specification;
    }
}
