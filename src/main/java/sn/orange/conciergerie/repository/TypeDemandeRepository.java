package sn.orange.conciergerie.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.TypeDemande;

/**
 * Spring Data JPA repository for the TypeDemande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeDemandeRepository extends JpaRepository<TypeDemande, UUID> {}
