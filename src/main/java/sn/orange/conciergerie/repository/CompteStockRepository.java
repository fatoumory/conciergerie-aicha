package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.CompteStock;

/**
 * Spring Data JPA repository for the CompteStock entity.
 */
@Repository
public interface CompteStockRepository extends JpaRepository<CompteStock, UUID> {
    default Optional<CompteStock> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CompteStock> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CompteStock> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select compteStock from CompteStock compteStock left join fetch compteStock.service",
        countQuery = "select count(compteStock) from CompteStock compteStock"
    )
    Page<CompteStock> findAllWithToOneRelationships(Pageable pageable);

    @Query("select compteStock from CompteStock compteStock left join fetch compteStock.service")
    List<CompteStock> findAllWithToOneRelationships();

    @Query("select compteStock from CompteStock compteStock left join fetch compteStock.service where compteStock.id =:id")
    Optional<CompteStock> findOneWithToOneRelationships(@Param("id") UUID id);
}
