package sn.orange.conciergerie.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.domain.TypeDemande;
import sn.orange.conciergerie.repository.TypeDemandeRepository;
import sn.orange.conciergerie.service.TypeDemandeService;
import sn.orange.conciergerie.service.dto.TypeDemandeDTO;
import sn.orange.conciergerie.service.mapper.TypeDemandeMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.TypeDemande}.
 */
@Service
@Transactional
public class TypeDemandeServiceImpl implements TypeDemandeService {

    private static final Logger LOG = LoggerFactory.getLogger(TypeDemandeServiceImpl.class);

    private final TypeDemandeRepository typeDemandeRepository;

    private final TypeDemandeMapper typeDemandeMapper;

    public TypeDemandeServiceImpl(TypeDemandeRepository typeDemandeRepository, TypeDemandeMapper typeDemandeMapper) {
        this.typeDemandeRepository = typeDemandeRepository;
        this.typeDemandeMapper = typeDemandeMapper;
    }

    @Override
    public TypeDemandeDTO save(TypeDemandeDTO typeDemandeDTO) {
        LOG.debug("Request to save TypeDemande : {}", typeDemandeDTO);
        TypeDemande typeDemande = typeDemandeMapper.toEntity(typeDemandeDTO);
        typeDemande = typeDemandeRepository.save(typeDemande);
        return typeDemandeMapper.toDto(typeDemande);
    }

    @Override
    public TypeDemandeDTO update(TypeDemandeDTO typeDemandeDTO) {
        LOG.debug("Request to update TypeDemande : {}", typeDemandeDTO);
        TypeDemande typeDemande = typeDemandeMapper.toEntity(typeDemandeDTO);
        typeDemande = typeDemandeRepository.save(typeDemande);
        return typeDemandeMapper.toDto(typeDemande);
    }

    @Override
    public Optional<TypeDemandeDTO> partialUpdate(TypeDemandeDTO typeDemandeDTO) {
        LOG.debug("Request to partially update TypeDemande : {}", typeDemandeDTO);

        return typeDemandeRepository
            .findById(typeDemandeDTO.getId())
            .map(existingTypeDemande -> {
                typeDemandeMapper.partialUpdate(existingTypeDemande, typeDemandeDTO);

                return existingTypeDemande;
            })
            .map(typeDemandeRepository::save)
            .map(typeDemandeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypeDemandeDTO> findAll() {
        LOG.debug("Request to get all TypeDemandes");
        return typeDemandeRepository.findAll().stream().map(typeDemandeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeDemandeDTO> findOne(UUID id) {
        LOG.debug("Request to get TypeDemande : {}", id);
        return typeDemandeRepository.findById(id).map(typeDemandeMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete TypeDemande : {}", id);
        typeDemandeRepository.deleteById(id);
    }
}
