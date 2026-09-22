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
import sn.orange.conciergerie.domain.Zone;
import sn.orange.conciergerie.repository.ZoneRepository;
import sn.orange.conciergerie.service.ZoneService;
import sn.orange.conciergerie.service.dto.ZoneDTO;
import sn.orange.conciergerie.service.mapper.ZoneMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.Zone}.
 */
@Service
@Transactional
public class ZoneServiceImpl implements ZoneService {

    private static final Logger LOG = LoggerFactory.getLogger(ZoneServiceImpl.class);

    private final ZoneRepository zoneRepository;

    private final ZoneMapper zoneMapper;

    public ZoneServiceImpl(ZoneRepository zoneRepository, ZoneMapper zoneMapper) {
        this.zoneRepository = zoneRepository;
        this.zoneMapper = zoneMapper;
    }

    @Override
    public ZoneDTO save(ZoneDTO zoneDTO) {
        LOG.debug("Request to save Zone : {}", zoneDTO);
        Zone zone = zoneMapper.toEntity(zoneDTO);
        zone = zoneRepository.save(zone);
        return zoneMapper.toDto(zone);
    }

    @Override
    public ZoneDTO update(ZoneDTO zoneDTO) {
        LOG.debug("Request to update Zone : {}", zoneDTO);
        Zone zone = zoneMapper.toEntity(zoneDTO);
        zone = zoneRepository.save(zone);
        return zoneMapper.toDto(zone);
    }

    @Override
    public Optional<ZoneDTO> partialUpdate(ZoneDTO zoneDTO) {
        LOG.debug("Request to partially update Zone : {}", zoneDTO);

        return zoneRepository
            .findById(zoneDTO.getId())
            .map(existingZone -> {
                zoneMapper.partialUpdate(existingZone, zoneDTO);

                return existingZone;
            })
            .map(zoneRepository::save)
            .map(zoneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ZoneDTO> findAll() {
        LOG.debug("Request to get all Zones");
        return zoneRepository.findAll().stream().map(zoneMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ZoneDTO> findOne(UUID id) {
        LOG.debug("Request to get Zone : {}", id);
        return zoneRepository.findById(id).map(zoneMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Zone : {}", id);
        zoneRepository.deleteById(id);
    }
}
