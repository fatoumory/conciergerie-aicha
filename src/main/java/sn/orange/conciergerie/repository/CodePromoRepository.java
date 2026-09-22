package sn.orange.conciergerie.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.CodePromo;

/**
 * Spring Data JPA repository for the CodePromo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodePromoRepository extends JpaRepository<CodePromo, UUID> {}
