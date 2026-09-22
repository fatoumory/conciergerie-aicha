package sn.orange.conciergerie.service.mapper;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.domain.TransactionPaiement;
import sn.orange.conciergerie.service.dto.DemandeDTO;
import sn.orange.conciergerie.service.dto.TransactionPaiementDTO;

/**
 * Mapper for the entity {@link TransactionPaiement} and its DTO {@link TransactionPaiementDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionPaiementMapper extends EntityMapper<TransactionPaiementDTO, TransactionPaiement> {
    @Mapping(target = "demande", source = "demande", qualifiedByName = "demandeId")
    TransactionPaiementDTO toDto(TransactionPaiement s);

    @Named("demandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DemandeDTO toDtoDemandeId(Demande demande);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
