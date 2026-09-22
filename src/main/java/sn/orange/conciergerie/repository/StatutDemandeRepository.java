package sn.orange.conciergerie.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.StatutDemande;

/**
 * Spring Data JPA repository for the StatutDemande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatutDemandeRepository extends JpaRepository<StatutDemande, UUID> {}
