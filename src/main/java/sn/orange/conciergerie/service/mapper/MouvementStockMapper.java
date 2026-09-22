package sn.orange.conciergerie.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import sn.orange.conciergerie.domain.CompteStock;
import sn.orange.conciergerie.domain.MouvementStock;
import sn.orange.conciergerie.service.dto.CompteStockDTO;
import sn.orange.conciergerie.service.dto.MouvementStockDTO;

/**
 * Mapper for the entity {@link MouvementStock} and its DTO {@link MouvementStockDTO}.
 */
@Mapper(componentModel = "spring")
public interface MouvementStockMapper extends EntityMapper<MouvementStockDTO, MouvementStock> {
    @Mapping(target = "compteStock", source = "compteStock", qualifiedByName = "compteStockId")
    MouvementStockDTO toDto(MouvementStock s);

    @Named("compteStockId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompteStockDTO toDtoCompteStockId(CompteStock compteStock);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
