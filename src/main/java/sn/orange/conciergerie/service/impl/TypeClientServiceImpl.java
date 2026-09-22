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
import sn.orange.conciergerie.domain.TypeClient;
import sn.orange.conciergerie.repository.TypeClientRepository;
import sn.orange.conciergerie.service.TypeClientService;
import sn.orange.conciergerie.service.dto.TypeClientDTO;
import sn.orange.conciergerie.service.mapper.TypeClientMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.TypeClient}.
 */
@Service
@Transactional
public class TypeClientServiceImpl implements TypeClientService {

    private static final Logger LOG = LoggerFactory.getLogger(TypeClientServiceImpl.class);

    private final TypeClientRepository typeClientRepository;

    private final TypeClientMapper typeClientMapper;

    public TypeClientServiceImpl(TypeClientRepository typeClientRepository, TypeClientMapper typeClientMapper) {
        this.typeClientRepository = typeClientRepository;
        this.typeClientMapper = typeClientMapper;
    }

    @Override
    public TypeClientDTO save(TypeClientDTO typeClientDTO) {
        LOG.debug("Request to save TypeClient : {}", typeClientDTO);
        TypeClient typeClient = typeClientMapper.toEntity(typeClientDTO);
        typeClient = typeClientRepository.save(typeClient);
        return typeClientMapper.toDto(typeClient);
    }

    @Override
    public TypeClientDTO update(TypeClientDTO typeClientDTO) {
        LOG.debug("Request to update TypeClient : {}", typeClientDTO);
        TypeClient typeClient = typeClientMapper.toEntity(typeClientDTO);
        typeClient = typeClientRepository.save(typeClient);
        return typeClientMapper.toDto(typeClient);
    }

    @Override
    public Optional<TypeClientDTO> partialUpdate(TypeClientDTO typeClientDTO) {
        LOG.debug("Request to partially update TypeClient : {}", typeClientDTO);

        return typeClientRepository
            .findById(typeClientDTO.getId())
            .map(existingTypeClient -> {
                typeClientMapper.partialUpdate(existingTypeClient, typeClientDTO);

                return existingTypeClient;
            })
            .map(typeClientRepository::save)
            .map(typeClientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypeClientDTO> findAll() {
        LOG.debug("Request to get all TypeClients");
        return typeClientRepository.findAll().stream().map(typeClientMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeClientDTO> findOne(UUID id) {
        LOG.debug("Request to get TypeClient : {}", id);
        return typeClientRepository.findById(id).map(typeClientMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete TypeClient : {}", id);
        typeClientRepository.deleteById(id);
    }
}
