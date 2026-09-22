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
import sn.orange.conciergerie.domain.TypeService;
import sn.orange.conciergerie.repository.TypeServiceRepository;
import sn.orange.conciergerie.service.TypeServiceService;
import sn.orange.conciergerie.service.dto.TypeServiceDTO;
import sn.orange.conciergerie.service.mapper.TypeServiceMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.TypeService}.
 */
@Service
@Transactional
public class TypeServiceServiceImpl implements TypeServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(TypeServiceServiceImpl.class);

    private final TypeServiceRepository typeServiceRepository;

    private final TypeServiceMapper typeServiceMapper;

    public TypeServiceServiceImpl(TypeServiceRepository typeServiceRepository, TypeServiceMapper typeServiceMapper) {
        this.typeServiceRepository = typeServiceRepository;
        this.typeServiceMapper = typeServiceMapper;
    }

    @Override
    public TypeServiceDTO save(TypeServiceDTO typeServiceDTO) {
        LOG.debug("Request to save TypeService : {}", typeServiceDTO);
        TypeService typeService = typeServiceMapper.toEntity(typeServiceDTO);
        typeService = typeServiceRepository.save(typeService);
        return typeServiceMapper.toDto(typeService);
    }

    @Override
    public TypeServiceDTO update(TypeServiceDTO typeServiceDTO) {
        LOG.debug("Request to update TypeService : {}", typeServiceDTO);
        TypeService typeService = typeServiceMapper.toEntity(typeServiceDTO);
        typeService = typeServiceRepository.save(typeService);
        return typeServiceMapper.toDto(typeService);
    }

    @Override
    public Optional<TypeServiceDTO> partialUpdate(TypeServiceDTO typeServiceDTO) {
        LOG.debug("Request to partially update TypeService : {}", typeServiceDTO);

        return typeServiceRepository
            .findById(typeServiceDTO.getId())
            .map(existingTypeService -> {
                typeServiceMapper.partialUpdate(existingTypeService, typeServiceDTO);

                return existingTypeService;
            })
            .map(typeServiceRepository::save)
            .map(typeServiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypeServiceDTO> findAll() {
        LOG.debug("Request to get all TypeServices");
        return typeServiceRepository.findAll().stream().map(typeServiceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeServiceDTO> findOne(UUID id) {
        LOG.debug("Request to get TypeService : {}", id);
        return typeServiceRepository.findById(id).map(typeServiceMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete TypeService : {}", id);
        typeServiceRepository.deleteById(id);
    }
}
