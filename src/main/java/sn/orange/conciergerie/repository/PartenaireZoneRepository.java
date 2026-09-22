package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.PartenaireZone;

/**
 * Spring Data JPA repository for the PartenaireZone entity.
 */
@Repository
public interface PartenaireZoneRepository extends JpaRepository<PartenaireZone, UUID> {
    default Optional<PartenaireZone> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PartenaireZone> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PartenaireZone> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select partenaireZone from PartenaireZone partenaireZone left join fetch partenaireZone.partenaire left join fetch partenaireZone.zone",
        countQuery = "select count(partenaireZone) from PartenaireZone partenaireZone"
    )
    Page<PartenaireZone> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select partenaireZone from PartenaireZone partenaireZone left join fetch partenaireZone.partenaire left join fetch partenaireZone.zone"
    )
    List<PartenaireZone> findAllWithToOneRelationships();

    @Query(
        "select partenaireZone from PartenaireZone partenaireZone left join fetch partenaireZone.partenaire left join fetch partenaireZone.zone where partenaireZone.id =:id"
    )
    Optional<PartenaireZone> findOneWithToOneRelationships(@Param("id") UUID id);
}
