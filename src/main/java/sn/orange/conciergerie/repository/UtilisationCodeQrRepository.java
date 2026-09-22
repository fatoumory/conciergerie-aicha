package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.orange.conciergerie.domain.UtilisationCodeQr;

/**
 * Spring Data JPA repository for the UtilisationCodeQr entity.
 */
@Repository
public interface UtilisationCodeQrRepository extends JpaRepository<UtilisationCodeQr, UUID> {
    default Optional<UtilisationCodeQr> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UtilisationCodeQr> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UtilisationCodeQr> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select utilisationCodeQr from UtilisationCodeQr utilisationCodeQr left join fetch utilisationCodeQr.codeQrService left join fetch utilisationCodeQr.partenaire",
        countQuery = "select count(utilisationCodeQr) from UtilisationCodeQr utilisationCodeQr"
    )
    Page<UtilisationCodeQr> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select utilisationCodeQr from UtilisationCodeQr utilisationCodeQr left join fetch utilisationCodeQr.codeQrService left join fetch utilisationCodeQr.partenaire"
    )
    List<UtilisationCodeQr> findAllWithToOneRelationships();

    @Query(
        "select utilisationCodeQr from UtilisationCodeQr utilisationCodeQr left join fetch utilisationCodeQr.codeQrService left join fetch utilisationCodeQr.partenaire where utilisationCodeQr.id =:id"
    )
    Optional<UtilisationCodeQr> findOneWithToOneRelationships(@Param("id") UUID id);
}
