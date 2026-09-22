package sn.orange.conciergerie.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import sn.orange.conciergerie.domain.CodeQrService;
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.service.dto.CodeQrServiceDTO;
import sn.orange.conciergerie.service.dto.DemandeDTO;

/**
 * Mapper for the entity {@link CodeQrService} and its DTO {@link CodeQrServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface CodeQrServiceMapper extends EntityMapper<CodeQrServiceDTO, CodeQrService> {
    @Mapping(target = "demande", source = "demande", qualifiedByName = "demandeId")
    CodeQrServiceDTO toDto(CodeQrService s);

    @Named("demandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DemandeDTO toDtoDemandeId(Demande demande);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
