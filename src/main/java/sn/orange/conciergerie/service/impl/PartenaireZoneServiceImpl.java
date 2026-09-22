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
import sn.orange.conciergerie.domain.PartenaireZone;
import sn.orange.conciergerie.repository.PartenaireZoneRepository;
import sn.orange.conciergerie.service.PartenaireZoneService;
import sn.orange.conciergerie.service.dto.PartenaireZoneDTO;
import sn.orange.conciergerie.service.mapper.PartenaireZoneMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.PartenaireZone}.
 */
@Service
@Transactional
public class PartenaireZoneServiceImpl implements PartenaireZoneService {

    private static final Logger LOG = LoggerFactory.getLogger(PartenaireZoneServiceImpl.class);

    private final PartenaireZoneRepository partenaireZoneRepository;

    private final PartenaireZoneMapper partenaireZoneMapper;

    public PartenaireZoneServiceImpl(PartenaireZoneRepository partenaireZoneRepository, PartenaireZoneMapper partenaireZoneMapper) {
        this.partenaireZoneRepository = partenaireZoneRepository;
        this.partenaireZoneMapper = partenaireZoneMapper;
    }

    @Override
    public PartenaireZoneDTO save(PartenaireZoneDTO partenaireZoneDTO) {
        LOG.debug("Request to save PartenaireZone : {}", partenaireZoneDTO);
        PartenaireZone partenaireZone = partenaireZoneMapper.toEntity(partenaireZoneDTO);
        partenaireZone = partenaireZoneRepository.save(partenaireZone);
        return partenaireZoneMapper.toDto(partenaireZone);
    }

    @Override
    public PartenaireZoneDTO update(PartenaireZoneDTO partenaireZoneDTO) {
        LOG.debug("Request to update PartenaireZone : {}", partenaireZoneDTO);
        PartenaireZone partenaireZone = partenaireZoneMapper.toEntity(partenaireZoneDTO);
        partenaireZone = partenaireZoneRepository.save(partenaireZone);
        return partenaireZoneMapper.toDto(partenaireZone);
    }

    @Override
    public Optional<PartenaireZoneDTO> partialUpdate(PartenaireZoneDTO partenaireZoneDTO) {
        LOG.debug("Request to partially update PartenaireZone : {}", partenaireZoneDTO);

        return partenaireZoneRepository
            .findById(partenaireZoneDTO.getId())
            .map(existingPartenaireZone -> {
                partenaireZoneMapper.partialUpdate(existingPartenaireZone, partenaireZoneDTO);

                return existingPartenaireZone;
            })
            .map(partenaireZoneRepository::save)
            .map(partenaireZoneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartenaireZoneDTO> findAll() {
        LOG.debug("Request to get all PartenaireZones");
        return partenaireZoneRepository
            .findAll()
            .stream()
            .map(partenaireZoneMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<PartenaireZoneDTO> findAllWithEagerRelationships() {
        return partenaireZoneRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(partenaireZoneMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PartenaireZoneDTO> findOne(UUID id) {
        LOG.debug("Request to get PartenaireZone : {}", id);
        return partenaireZoneRepository.findOneWithEagerRelationships(id).map(partenaireZoneMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete PartenaireZone : {}", id);
        partenaireZoneRepository.deleteById(id);
    }
}
