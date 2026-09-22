package sn.orange.conciergerie.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import sn.orange.conciergerie.domain.EligibiliteService;
import sn.orange.conciergerie.domain.QuotaService;
import sn.orange.conciergerie.service.dto.EligibiliteServiceDTO;
import sn.orange.conciergerie.service.dto.QuotaServiceDTO;

/**
 * Mapper for the entity {@link QuotaService} and its DTO {@link QuotaServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuotaServiceMapper extends EntityMapper<QuotaServiceDTO, QuotaService> {
    @Mapping(target = "eligibiliteService", source = "eligibiliteService", qualifiedByName = "eligibiliteServiceId")
    QuotaServiceDTO toDto(QuotaService s);

    @Named("eligibiliteServiceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EligibiliteServiceDTO toDtoEligibiliteServiceId(EligibiliteService eligibiliteService);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
