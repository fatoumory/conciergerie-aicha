package sn.orange.conciergerie.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.IdempotencyKey;

/**
 * Spring Data JPA repository for the IdempotencyKey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, UUID> {}
