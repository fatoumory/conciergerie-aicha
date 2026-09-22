package sn.orange.conciergerie.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.MouvementStock;

/**
 * Spring Data JPA repository for the MouvementStock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock, UUID> {}
