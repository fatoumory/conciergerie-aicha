package sn.orange.conciergerie.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.CodeQrService;

/**
 * Spring Data JPA repository for the CodeQrService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeQrServiceRepository extends JpaRepository<CodeQrService, UUID> {}
