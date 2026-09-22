package sn.orange.conciergerie.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.QuotaService;

/**
 * Spring Data JPA repository for the QuotaService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuotaServiceRepository extends JpaRepository<QuotaService, UUID> {}
