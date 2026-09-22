package sn.orange.conciergerie.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.TypeClient;

/**
 * Spring Data JPA repository for the TypeClient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeClientRepository extends JpaRepository<TypeClient, UUID> {}
