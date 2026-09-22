package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.Client;
import sn.orange.conciergerie.domain.SegmentClient;
import sn.orange.conciergerie.domain.TypeClient;
import sn.orange.conciergerie.domain.User;
import sn.orange.conciergerie.service.dto.ClientDTO;
import sn.orange.conciergerie.service.dto.SegmentClientDTO;
import sn.orange.conciergerie.service.dto.TypeClientDTO;
import sn.orange.conciergerie.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "typeClient", source = "typeClient", qualifiedByName = "typeClientLibelle")
    @Mapping(target = "segmentClient", source = "segmentClient", qualifiedByName = "segmentClientLibelle")
    ClientDTO toDto(Client s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("typeClientLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    TypeClientDTO toDtoTypeClientLibelle(TypeClient typeClient);

    @Named("segmentClientLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    SegmentClientDTO toDtoSegmentClientLibelle(SegmentClient segmentClient);
}
