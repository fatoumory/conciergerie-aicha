package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.EligibiliteService;

/**
 * Spring Data JPA repository for the EligibiliteService entity.
 */
@Repository
public interface EligibiliteServiceRepository extends JpaRepository<EligibiliteService, UUID> {
    default Optional<EligibiliteService> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EligibiliteService> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EligibiliteService> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select eligibiliteService from EligibiliteService eligibiliteService left join fetch eligibiliteService.service left join fetch eligibiliteService.segmentClient left join fetch eligibiliteService.typeClient",
        countQuery = "select count(eligibiliteService) from EligibiliteService eligibiliteService"
    )
    Page<EligibiliteService> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select eligibiliteService from EligibiliteService eligibiliteService left join fetch eligibiliteService.service left join fetch eligibiliteService.segmentClient left join fetch eligibiliteService.typeClient"
    )
    List<EligibiliteService> findAllWithToOneRelationships();

    @Query(
        "select eligibiliteService from EligibiliteService eligibiliteService left join fetch eligibiliteService.service left join fetch eligibiliteService.segmentClient left join fetch eligibiliteService.typeClient where eligibiliteService.id =:id"
    )
    Optional<EligibiliteService> findOneWithToOneRelationships(@Param("id") UUID id);
}
