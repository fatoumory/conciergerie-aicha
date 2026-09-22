package sn.orange.conciergerie.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sn.orange.conciergerie.domain.Authority;
import sn.orange.conciergerie.domain.Profil;
import sn.orange.conciergerie.domain.User;
import sn.orange.conciergerie.service.dto.AuthorityDTO;
import sn.orange.conciergerie.service.dto.ProfilDTO;
import sn.orange.conciergerie.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Profil} and its DTO {@link ProfilDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfilMapper extends EntityMapper<ProfilDTO, Profil> {
    @Mapping(target = "utilisateurs", source = "utilisateurs", qualifiedByName = "userLoginSet")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "authorityNameSet")
    ProfilDTO toDto(Profil s);

    @Mapping(target = "removeUtilisateur", ignore = true)
    @Mapping(target = "removeRole", ignore = true)
    Profil toEntity(ProfilDTO profilDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("userLoginSet")
    default Set<UserDTO> toDtoUserLoginSet(Set<User> user) {
        return user.stream().map(this::toDtoUserLogin).collect(Collectors.toSet());
    }

    @Named("authorityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    AuthorityDTO toDtoAuthorityName(Authority authority);

    @Named("authorityNameSet")
    default Set<AuthorityDTO> toDtoAuthorityNameSet(Set<Authority> authority) {
        return authority.stream().map(this::toDtoAuthorityName).collect(Collectors.toSet());
    }
}
