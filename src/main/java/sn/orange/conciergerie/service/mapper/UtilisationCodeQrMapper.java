package sn.orange.conciergerie.service.mapper;

import org.mapstruct.*;
import sn.orange.conciergerie.domain.CodeQrService;
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.domain.UtilisationCodeQr;
import sn.orange.conciergerie.service.dto.CodeQrServiceDTO;
import sn.orange.conciergerie.service.dto.PartenaireDTO;
import sn.orange.conciergerie.service.dto.UtilisationCodeQrDTO;

/**
 * Mapper for the entity {@link UtilisationCodeQr} and its DTO {@link UtilisationCodeQrDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilisationCodeQrMapper extends EntityMapper<UtilisationCodeQrDTO, UtilisationCodeQr> {
    @Mapping(target = "codeQrService", source = "codeQrService", qualifiedByName = "codeQrServiceCode")
    @Mapping(target = "partenaire", source = "partenaire", qualifiedByName = "partenaireLibelle")
    UtilisationCodeQrDTO toDto(UtilisationCodeQr s);

    @Named("codeQrServiceCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    CodeQrServiceDTO toDtoCodeQrServiceCode(CodeQrService codeQrService);

    @Named("partenaireLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    PartenaireDTO toDtoPartenaireLibelle(Partenaire partenaire);
}
