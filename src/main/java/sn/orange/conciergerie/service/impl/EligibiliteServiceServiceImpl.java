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
import sn.orange.conciergerie.domain.EligibiliteService;
import sn.orange.conciergerie.repository.EligibiliteServiceRepository;
import sn.orange.conciergerie.service.EligibiliteServiceService;
import sn.orange.conciergerie.service.dto.EligibiliteServiceDTO;
import sn.orange.conciergerie.service.mapper.EligibiliteServiceMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.EligibiliteService}.
 */
@Service
@Transactional
public class EligibiliteServiceServiceImpl implements EligibiliteServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(EligibiliteServiceServiceImpl.class);

    private final EligibiliteServiceRepository eligibiliteServiceRepository;

    private final EligibiliteServiceMapper eligibiliteServiceMapper;

    public EligibiliteServiceServiceImpl(
        EligibiliteServiceRepository eligibiliteServiceRepository,
        EligibiliteServiceMapper eligibiliteServiceMapper
    ) {
        this.eligibiliteServiceRepository = eligibiliteServiceRepository;
        this.eligibiliteServiceMapper = eligibiliteServiceMapper;
    }

    @Override
    public EligibiliteServiceDTO save(EligibiliteServiceDTO eligibiliteServiceDTO) {
        LOG.debug("Request to save EligibiliteService : {}", eligibiliteServiceDTO);
        EligibiliteService eligibiliteService = eligibiliteServiceMapper.toEntity(eligibiliteServiceDTO);
        eligibiliteService = eligibiliteServiceRepository.save(eligibiliteService);
        return eligibiliteServiceMapper.toDto(eligibiliteService);
    }

    @Override
    public EligibiliteServiceDTO update(EligibiliteServiceDTO eligibiliteServiceDTO) {
        LOG.debug("Request to update EligibiliteService : {}", eligibiliteServiceDTO);
        EligibiliteService eligibiliteService = eligibiliteServiceMapper.toEntity(eligibiliteServiceDTO);
        eligibiliteService = eligibiliteServiceRepository.save(eligibiliteService);
        return eligibiliteServiceMapper.toDto(eligibiliteService);
    }

    @Override
    public Optional<EligibiliteServiceDTO> partialUpdate(EligibiliteServiceDTO eligibiliteServiceDTO) {
        LOG.debug("Request to partially update EligibiliteService : {}", eligibiliteServiceDTO);

        return eligibiliteServiceRepository
            .findById(eligibiliteServiceDTO.getId())
            .map(existingEligibiliteService -> {
                eligibiliteServiceMapper.partialUpdate(existingEligibiliteService, eligibiliteServiceDTO);

                return existingEligibiliteService;
            })
            .map(eligibiliteServiceRepository::save)
            .map(eligibiliteServiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EligibiliteServiceDTO> findAll() {
        LOG.debug("Request to get all EligibiliteServices");
        return eligibiliteServiceRepository
            .findAll()
            .stream()
            .map(eligibiliteServiceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<EligibiliteServiceDTO> findAllWithEagerRelationships() {
        return eligibiliteServiceRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(eligibiliteServiceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EligibiliteServiceDTO> findOne(UUID id) {
        LOG.debug("Request to get EligibiliteService : {}", id);
        return eligibiliteServiceRepository.findOneWithEagerRelationships(id).map(eligibiliteServiceMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete EligibiliteService : {}", id);
        eligibiliteServiceRepository.deleteById(id);
    }
}
