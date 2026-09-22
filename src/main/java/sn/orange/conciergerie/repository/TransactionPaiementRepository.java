package sn.orange.conciergerie.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.TransactionPaiement;

/**
 * Spring Data JPA repository for the TransactionPaiement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionPaiementRepository
    extends JpaRepository<TransactionPaiement, UUID>, JpaSpecificationExecutor<TransactionPaiement> {}
