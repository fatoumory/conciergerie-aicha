package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.HistoriqueStatutDemande;

/**
 * Spring Data JPA repository for the HistoriqueStatutDemande entity.
 */
@Repository
public interface HistoriqueStatutDemandeRepository extends JpaRepository<HistoriqueStatutDemande, UUID> {
    default Optional<HistoriqueStatutDemande> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<HistoriqueStatutDemande> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<HistoriqueStatutDemande> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select historiqueStatutDemande from HistoriqueStatutDemande historiqueStatutDemande left join fetch historiqueStatutDemande.statut",
        countQuery = "select count(historiqueStatutDemande) from HistoriqueStatutDemande historiqueStatutDemande"
    )
    Page<HistoriqueStatutDemande> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select historiqueStatutDemande from HistoriqueStatutDemande historiqueStatutDemande left join fetch historiqueStatutDemande.statut"
    )
    List<HistoriqueStatutDemande> findAllWithToOneRelationships();

    @Query(
        "select historiqueStatutDemande from HistoriqueStatutDemande historiqueStatutDemande left join fetch historiqueStatutDemande.statut where historiqueStatutDemande.id =:id"
    )
    Optional<HistoriqueStatutDemande> findOneWithToOneRelationships(@Param("id") UUID id);
}
