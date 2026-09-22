package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.CouverturePartenaire;

/**
 * Spring Data JPA repository for the CouverturePartenaire entity.
 */
@Repository
public interface CouverturePartenaireRepository extends JpaRepository<CouverturePartenaire, UUID> {
    default Optional<CouverturePartenaire> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CouverturePartenaire> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CouverturePartenaire> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select couverturePartenaire from CouverturePartenaire couverturePartenaire left join fetch couverturePartenaire.partenaire left join fetch couverturePartenaire.service",
        countQuery = "select count(couverturePartenaire) from CouverturePartenaire couverturePartenaire"
    )
    Page<CouverturePartenaire> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select couverturePartenaire from CouverturePartenaire couverturePartenaire left join fetch couverturePartenaire.partenaire left join fetch couverturePartenaire.service"
    )
    List<CouverturePartenaire> findAllWithToOneRelationships();

    @Query(
        "select couverturePartenaire from CouverturePartenaire couverturePartenaire left join fetch couverturePartenaire.partenaire left join fetch couverturePartenaire.service where couverturePartenaire.id =:id"
    )
    Optional<CouverturePartenaire> findOneWithToOneRelationships(@Param("id") UUID id);
}
