package sn.orange.conciergerie.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.JournalAudit;

/**
 * Spring Data JPA repository for the JournalAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JournalAuditRepository extends JpaRepository<JournalAudit, UUID> {}
