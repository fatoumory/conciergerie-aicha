package sn.orange.conciergerie.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.TypeService;

/**
 * Spring Data JPA repository for the TypeService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeServiceRepository extends JpaRepository<TypeService, UUID> {}
