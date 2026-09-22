package sn.orange.conciergerie.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import sn.orange.conciergerie.domain.Profil;

public interface ProfilRepositoryWithBagRelationships {
    Optional<Profil> fetchBagRelationships(Optional<Profil> profil);

    List<Profil> fetchBagRelationships(List<Profil> profils);

    Page<Profil> fetchBagRelationships(Page<Profil> profils);
}
