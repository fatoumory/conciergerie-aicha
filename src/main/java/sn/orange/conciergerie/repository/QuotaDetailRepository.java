package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.QuotaDetail;

/**
 * Spring Data JPA repository for the QuotaDetail entity.
 */
@Repository
public interface QuotaDetailRepository extends JpaRepository<QuotaDetail, UUID> {
    default Optional<QuotaDetail> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<QuotaDetail> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<QuotaDetail> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select quotaDetail from QuotaDetail quotaDetail left join fetch quotaDetail.zone",
        countQuery = "select count(quotaDetail) from QuotaDetail quotaDetail"
    )
    Page<QuotaDetail> findAllWithToOneRelationships(Pageable pageable);

    @Query("select quotaDetail from QuotaDetail quotaDetail left join fetch quotaDetail.zone")
    List<QuotaDetail> findAllWithToOneRelationships();

    @Query("select quotaDetail from QuotaDetail quotaDetail left join fetch quotaDetail.zone where quotaDetail.id =:id")
    Optional<QuotaDetail> findOneWithToOneRelationships(@Param("id") UUID id);
}
