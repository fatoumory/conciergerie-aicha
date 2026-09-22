package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.AffectationDemande;

/**
 * Spring Data JPA repository for the AffectationDemande entity.
 */
@Repository
public interface AffectationDemandeRepository extends JpaRepository<AffectationDemande, UUID> {
    default Optional<AffectationDemande> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AffectationDemande> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AffectationDemande> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select affectationDemande from AffectationDemande affectationDemande left join fetch affectationDemande.partenaire",
        countQuery = "select count(affectationDemande) from AffectationDemande affectationDemande"
    )
    Page<AffectationDemande> findAllWithToOneRelationships(Pageable pageable);

    @Query("select affectationDemande from AffectationDemande affectationDemande left join fetch affectationDemande.partenaire")
    List<AffectationDemande> findAllWithToOneRelationships();

    @Query(
        "select affectationDemande from AffectationDemande affectationDemande left join fetch affectationDemande.partenaire where affectationDemande.id =:id"
    )
    Optional<AffectationDemande> findOneWithToOneRelationships(@Param("id") UUID id);
}
