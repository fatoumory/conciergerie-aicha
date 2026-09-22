package sn.orange.conciergerie.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import sn.orange.conciergerie.domain.Client;
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.domain.Notification;
import sn.orange.conciergerie.service.dto.ClientDTO;
import sn.orange.conciergerie.service.dto.DemandeDTO;
import sn.orange.conciergerie.service.dto.NotificationDTO;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientNumero")
    @Mapping(target = "demande", source = "demande", qualifiedByName = "demandeId")
    NotificationDTO toDto(Notification s);

    @Named("clientNumero")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numero", source = "numero")
    ClientDTO toDtoClientNumero(Client client);

    @Named("demandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DemandeDTO toDtoDemandeId(Demande demande);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
