package sn.orange.conciergerie.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.domain.ServiceConciergerie;
import sn.orange.conciergerie.repository.ServiceConciergerieRepository;
import sn.orange.conciergerie.service.ServiceConciergerieService;
import sn.orange.conciergerie.service.dto.ServiceConciergerieDTO;
import sn.orange.conciergerie.service.mapper.ServiceConciergerieMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.ServiceConciergerie}.
 */
@Service
@Transactional
public class ServiceConciergerieServiceImpl implements ServiceConciergerieService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceConciergerieServiceImpl.class);

    private final ServiceConciergerieRepository serviceConciergerieRepository;

    private final ServiceConciergerieMapper serviceConciergerieMapper;

    public ServiceConciergerieServiceImpl(
        ServiceConciergerieRepository serviceConciergerieRepository,
        ServiceConciergerieMapper serviceConciergerieMapper
    ) {
        this.serviceConciergerieRepository = serviceConciergerieRepository;
        this.serviceConciergerieMapper = serviceConciergerieMapper;
    }

    @Override
    public ServiceConciergerieDTO save(ServiceConciergerieDTO serviceConciergerieDTO) {
        LOG.debug("Request to save ServiceConciergerie : {}", serviceConciergerieDTO);
        ServiceConciergerie serviceConciergerie = serviceConciergerieMapper.toEntity(serviceConciergerieDTO);
        serviceConciergerie = serviceConciergerieRepository.save(serviceConciergerie);
        return serviceConciergerieMapper.toDto(serviceConciergerie);
    }

    @Override
    public ServiceConciergerieDTO update(ServiceConciergerieDTO serviceConciergerieDTO) {
        LOG.debug("Request to update ServiceConciergerie : {}", serviceConciergerieDTO);
        ServiceConciergerie serviceConciergerie = serviceConciergerieMapper.toEntity(serviceConciergerieDTO);
        serviceConciergerie = serviceConciergerieRepository.save(serviceConciergerie);
        return serviceConciergerieMapper.toDto(serviceConciergerie);
    }

    @Override
    public Optional<ServiceConciergerieDTO> partialUpdate(ServiceConciergerieDTO serviceConciergerieDTO) {
        LOG.debug("Request to partially update ServiceConciergerie : {}", serviceConciergerieDTO);

        return serviceConciergerieRepository
            .findById(serviceConciergerieDTO.getId())
            .map(existingServiceConciergerie -> {
                serviceConciergerieMapper.partialUpdate(existingServiceConciergerie, serviceConciergerieDTO);

                return existingServiceConciergerie;
            })
            .map(serviceConciergerieRepository::save)
            .map(serviceConciergerieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceConciergerieDTO> findAll() {
        LOG.debug("Request to get all ServiceConciergeries");
        return serviceConciergerieRepository
            .findAll()
            .stream()
            .map(serviceConciergerieMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<ServiceConciergerieDTO> findAllWithEagerRelationships() {
        return serviceConciergerieRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(serviceConciergerieMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the serviceConciergeries where CompteStock is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ServiceConciergerieDTO> findAllWhereCompteStockIsNull() {
        LOG.debug("Request to get all serviceConciergeries where CompteStock is null");
        return StreamSupport.stream(serviceConciergerieRepository.findAll().spliterator(), false)
            .filter(serviceConciergerie -> serviceConciergerie.getCompteStock() == null)
            .map(serviceConciergerieMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceConciergerieDTO> findOne(UUID id) {
        LOG.debug("Request to get ServiceConciergerie : {}", id);
        return serviceConciergerieRepository.findOneWithEagerRelationships(id).map(serviceConciergerieMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete ServiceConciergerie : {}", id);
        serviceConciergerieRepository.deleteById(id);
    }
}
