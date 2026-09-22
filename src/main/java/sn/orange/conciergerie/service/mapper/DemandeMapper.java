package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.Client;
import sn.orange.conciergerie.domain.CodePromo;
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.domain.ServiceConciergerie;
import sn.orange.conciergerie.domain.StatutDemande;
import sn.orange.conciergerie.domain.TypeDemande;
import sn.orange.conciergerie.service.dto.ClientDTO;
import sn.orange.conciergerie.service.dto.CodePromoDTO;
import sn.orange.conciergerie.service.dto.DemandeDTO;
import sn.orange.conciergerie.service.dto.ServiceConciergerieDTO;
import sn.orange.conciergerie.service.dto.StatutDemandeDTO;
import sn.orange.conciergerie.service.dto.TypeDemandeDTO;

/**
 * Mapper for the entity {@link Demande} and its DTO {@link DemandeDTO}.
 */
@Mapper(componentModel = "spring")
public interface DemandeMapper extends EntityMapper<DemandeDTO, Demande> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientNumero")
    @Mapping(target = "service", source = "service", qualifiedByName = "serviceConciergerieLibelle")
    @Mapping(target = "typeDemande", source = "typeDemande", qualifiedByName = "typeDemandeLibelle")
    @Mapping(target = "statut", source = "statut", qualifiedByName = "statutDemandeLibelle")
    @Mapping(target = "codePromo", source = "codePromo", qualifiedByName = "codePromoCode")
    DemandeDTO toDto(Demande s);

    @Named("clientNumero")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numero", source = "numero")
    ClientDTO toDtoClientNumero(Client client);

    @Named("serviceConciergerieLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    ServiceConciergerieDTO toDtoServiceConciergerieLibelle(ServiceConciergerie serviceConciergerie);

    @Named("typeDemandeLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    TypeDemandeDTO toDtoTypeDemandeLibelle(TypeDemande typeDemande);

    @Named("statutDemandeLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    StatutDemandeDTO toDtoStatutDemandeLibelle(StatutDemande statutDemande);

    @Named("codePromoCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    CodePromoDTO toDtoCodePromoCode(CodePromo codePromo);
}
