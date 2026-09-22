package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.ServiceConciergerie;

/**
 * Spring Data JPA repository for the ServiceConciergerie entity.
 */
@Repository
public interface ServiceConciergerieRepository extends JpaRepository<ServiceConciergerie, UUID> {
    default Optional<ServiceConciergerie> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ServiceConciergerie> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ServiceConciergerie> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select serviceConciergerie from ServiceConciergerie serviceConciergerie left join fetch serviceConciergerie.typeService",
        countQuery = "select count(serviceConciergerie) from ServiceConciergerie serviceConciergerie"
    )
    Page<ServiceConciergerie> findAllWithToOneRelationships(Pageable pageable);

    @Query("select serviceConciergerie from ServiceConciergerie serviceConciergerie left join fetch serviceConciergerie.typeService")
    List<ServiceConciergerie> findAllWithToOneRelationships();

    @Query(
        "select serviceConciergerie from ServiceConciergerie serviceConciergerie left join fetch serviceConciergerie.typeService where serviceConciergerie.id =:id"
    )
    Optional<ServiceConciergerie> findOneWithToOneRelationships(@Param("id") UUID id);
}
