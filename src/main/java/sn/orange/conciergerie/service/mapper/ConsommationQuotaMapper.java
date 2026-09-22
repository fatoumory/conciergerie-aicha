package sn.orange.conciergerie.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import sn.orange.conciergerie.domain.Client;
import sn.orange.conciergerie.domain.ConsommationQuota;
import sn.orange.conciergerie.domain.QuotaDetail;
import sn.orange.conciergerie.domain.QuotaService;
import sn.orange.conciergerie.service.dto.ClientDTO;
import sn.orange.conciergerie.service.dto.ConsommationQuotaDTO;
import sn.orange.conciergerie.service.dto.QuotaDetailDTO;
import sn.orange.conciergerie.service.dto.QuotaServiceDTO;

/**
 * Mapper for the entity {@link ConsommationQuota} and its DTO {@link ConsommationQuotaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConsommationQuotaMapper extends EntityMapper<ConsommationQuotaDTO, ConsommationQuota> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientNumero")
    @Mapping(target = "quotaService", source = "quotaService", qualifiedByName = "quotaServiceId")
    @Mapping(target = "quotaDetail", source = "quotaDetail", qualifiedByName = "quotaDetailId")
    ConsommationQuotaDTO toDto(ConsommationQuota s);

    @Named("clientNumero")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numero", source = "numero")
    ClientDTO toDtoClientNumero(Client client);

    @Named("quotaServiceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuotaServiceDTO toDtoQuotaServiceId(QuotaService quotaService);

    @Named("quotaDetailId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuotaDetailDTO toDtoQuotaDetailId(QuotaDetail quotaDetail);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
