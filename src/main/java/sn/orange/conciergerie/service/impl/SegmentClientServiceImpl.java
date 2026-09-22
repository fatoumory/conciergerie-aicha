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
import sn.orange.conciergerie.domain.SegmentClient;
import sn.orange.conciergerie.repository.SegmentClientRepository;
import sn.orange.conciergerie.service.SegmentClientService;
import sn.orange.conciergerie.service.dto.SegmentClientDTO;
import sn.orange.conciergerie.service.mapper.SegmentClientMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.SegmentClient}.
 */
@Service
@Transactional
public class SegmentClientServiceImpl implements SegmentClientService {

    private static final Logger LOG = LoggerFactory.getLogger(SegmentClientServiceImpl.class);

    private final SegmentClientRepository segmentClientRepository;

    private final SegmentClientMapper segmentClientMapper;

    public SegmentClientServiceImpl(SegmentClientRepository segmentClientRepository, SegmentClientMapper segmentClientMapper) {
        this.segmentClientRepository = segmentClientRepository;
        this.segmentClientMapper = segmentClientMapper;
    }

    @Override
    public SegmentClientDTO save(SegmentClientDTO segmentClientDTO) {
        LOG.debug("Request to save SegmentClient : {}", segmentClientDTO);
        SegmentClient segmentClient = segmentClientMapper.toEntity(segmentClientDTO);
        segmentClient = segmentClientRepository.save(segmentClient);
        return segmentClientMapper.toDto(segmentClient);
    }

    @Override
    public SegmentClientDTO update(SegmentClientDTO segmentClientDTO) {
        LOG.debug("Request to update SegmentClient : {}", segmentClientDTO);
        SegmentClient segmentClient = segmentClientMapper.toEntity(segmentClientDTO);
        segmentClient = segmentClientRepository.save(segmentClient);
        return segmentClientMapper.toDto(segmentClient);
    }

    @Override
    public Optional<SegmentClientDTO> partialUpdate(SegmentClientDTO segmentClientDTO) {
        LOG.debug("Request to partially update SegmentClient : {}", segmentClientDTO);

        return segmentClientRepository
            .findById(segmentClientDTO.getId())
            .map(existingSegmentClient -> {
                segmentClientMapper.partialUpdate(existingSegmentClient, segmentClientDTO);

                return existingSegmentClient;
            })
            .map(segmentClientRepository::save)
            .map(segmentClientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SegmentClientDTO> findAll() {
        LOG.debug("Request to get all SegmentClients");
        return segmentClientRepository.findAll().stream().map(segmentClientMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SegmentClientDTO> findOne(UUID id) {
        LOG.debug("Request to get SegmentClient : {}", id);
        return segmentClientRepository.findById(id).map(segmentClientMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete SegmentClient : {}", id);
        segmentClientRepository.deleteById(id);
    }
}
