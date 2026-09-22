package sn.orange.conciergerie.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.SegmentClient;

/**
 * Spring Data JPA repository for the SegmentClient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SegmentClientRepository extends JpaRepository<SegmentClient, UUID> {}
