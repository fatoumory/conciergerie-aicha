package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.Demande;

/**
 * Spring Data JPA repository for the Demande entity.
 */
@Repository
public interface DemandeRepository extends JpaRepository<Demande, UUID>, JpaSpecificationExecutor<Demande> {
    default Optional<Demande> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Demande> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Demande> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select demande from Demande demande left join fetch demande.client left join fetch demande.service left join fetch demande.typeDemande left join fetch demande.statut left join fetch demande.codePromo",
        countQuery = "select count(demande) from Demande demande"
    )
    Page<Demande> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select demande from Demande demande left join fetch demande.client left join fetch demande.service left join fetch demande.typeDemande left join fetch demande.statut left join fetch demande.codePromo"
    )
    List<Demande> findAllWithToOneRelationships();

    @Query(
        "select demande from Demande demande left join fetch demande.client left join fetch demande.service left join fetch demande.typeDemande left join fetch demande.statut left join fetch demande.codePromo where demande.id =:id"
    )
    Optional<Demande> findOneWithToOneRelationships(@Param("id") UUID id);
}
