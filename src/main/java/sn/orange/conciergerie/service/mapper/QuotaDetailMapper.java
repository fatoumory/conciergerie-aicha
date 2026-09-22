package sn.orange.conciergerie.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import sn.orange.conciergerie.domain.QuotaDetail;
import sn.orange.conciergerie.domain.QuotaService;
import sn.orange.conciergerie.domain.Zone;
import sn.orange.conciergerie.service.dto.QuotaDetailDTO;
import sn.orange.conciergerie.service.dto.QuotaServiceDTO;
import sn.orange.conciergerie.service.dto.ZoneDTO;

/**
 * Mapper for the entity {@link QuotaDetail} and its DTO {@link QuotaDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuotaDetailMapper extends EntityMapper<QuotaDetailDTO, QuotaDetail> {
    @Mapping(target = "quotaService", source = "quotaService", qualifiedByName = "quotaServiceId")
    @Mapping(target = "zone", source = "zone", qualifiedByName = "zoneLibelle")
    QuotaDetailDTO toDto(QuotaDetail s);

    @Named("quotaServiceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuotaServiceDTO toDtoQuotaServiceId(QuotaService quotaService);

    @Named("zoneLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    ZoneDTO toDtoZoneLibelle(Zone zone);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
