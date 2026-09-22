package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.Prestation;

/**
 * Spring Data JPA repository for the Prestation entity.
 */
@Repository
public interface PrestationRepository extends JpaRepository<Prestation, UUID> {
    default Optional<Prestation> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Prestation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Prestation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select prestation from Prestation prestation left join fetch prestation.partenaire",
        countQuery = "select count(prestation) from Prestation prestation"
    )
    Page<Prestation> findAllWithToOneRelationships(Pageable pageable);

    @Query("select prestation from Prestation prestation left join fetch prestation.partenaire")
    List<Prestation> findAllWithToOneRelationships();

    @Query("select prestation from Prestation prestation left join fetch prestation.partenaire where prestation.id =:id")
    Optional<Prestation> findOneWithToOneRelationships(@Param("id") UUID id);
}
