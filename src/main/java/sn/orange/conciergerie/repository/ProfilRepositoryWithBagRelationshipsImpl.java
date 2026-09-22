package sn.orange.conciergerie.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import sn.orange.conciergerie.domain.Profil;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProfilRepositoryWithBagRelationshipsImpl implements ProfilRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PROFILS_PARAMETER = "profils";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Profil> fetchBagRelationships(Optional<Profil> profil) {
        return profil.map(this::fetchUtilisateurs).map(this::fetchRoles);
    }

    @Override
    public Page<Profil> fetchBagRelationships(Page<Profil> profils) {
        return new PageImpl<>(fetchBagRelationships(profils.getContent()), profils.getPageable(), profils.getTotalElements());
    }

    @Override
    public List<Profil> fetchBagRelationships(List<Profil> profils) {
        return Optional.of(profils).map(this::fetchUtilisateurs).map(this::fetchRoles).orElse(List.of());
    }

    Profil fetchUtilisateurs(Profil result) {
        return entityManager
            .createQuery("select profil from Profil profil left join fetch profil.utilisateurs where profil.id = :id", Profil.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Profil> fetchUtilisateurs(List<Profil> profils) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, profils.size()).forEach(index -> order.put(profils.get(index).getId(), index));
        List<Profil> result = entityManager
            .createQuery("select profil from Profil profil left join fetch profil.utilisateurs where profil in :profils", Profil.class)
            .setParameter(PROFILS_PARAMETER, profils)
            .getResultList();
        result.sort((o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Profil fetchRoles(Profil result) {
        return entityManager
            .createQuery("select profil from Profil profil left join fetch profil.roles where profil.id = :id", Profil.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Profil> fetchRoles(List<Profil> profils) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, profils.size()).forEach(index -> order.put(profils.get(index).getId(), index));
        List<Profil> result = entityManager
            .createQuery("select profil from Profil profil left join fetch profil.roles where profil in :profils", Profil.class)
            .setParameter(PROFILS_PARAMETER, profils)
            .getResultList();
        result.sort((o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
