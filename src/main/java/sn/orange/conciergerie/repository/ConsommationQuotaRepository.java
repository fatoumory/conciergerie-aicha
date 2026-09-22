package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.ConsommationQuota;

/**
 * Spring Data JPA repository for the ConsommationQuota entity.
 */
@Repository
public interface ConsommationQuotaRepository extends JpaRepository<ConsommationQuota, UUID>, JpaSpecificationExecutor<ConsommationQuota> {
    default Optional<ConsommationQuota> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ConsommationQuota> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ConsommationQuota> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select consommationQuota from ConsommationQuota consommationQuota left join fetch consommationQuota.client",
        countQuery = "select count(consommationQuota) from ConsommationQuota consommationQuota"
    )
    Page<ConsommationQuota> findAllWithToOneRelationships(Pageable pageable);

    @Query("select consommationQuota from ConsommationQuota consommationQuota left join fetch consommationQuota.client")
    List<ConsommationQuota> findAllWithToOneRelationships();

    @Query(
        "select consommationQuota from ConsommationQuota consommationQuota left join fetch consommationQuota.client where consommationQuota.id =:id"
    )
    Optional<ConsommationQuota> findOneWithToOneRelationships(@Param("id") UUID id);
}
