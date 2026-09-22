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
import sn.orange.conciergerie.domain.Prestation;
import sn.orange.conciergerie.repository.PrestationRepository;
import sn.orange.conciergerie.service.PrestationService;
import sn.orange.conciergerie.service.dto.PrestationDTO;
import sn.orange.conciergerie.service.mapper.PrestationMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.Prestation}.
 */
@Service
@Transactional
public class PrestationServiceImpl implements PrestationService {

    private static final Logger LOG = LoggerFactory.getLogger(PrestationServiceImpl.class);

    private final PrestationRepository prestationRepository;

    private final PrestationMapper prestationMapper;

    public PrestationServiceImpl(PrestationRepository prestationRepository, PrestationMapper prestationMapper) {
        this.prestationRepository = prestationRepository;
        this.prestationMapper = prestationMapper;
    }

    @Override
    public PrestationDTO save(PrestationDTO prestationDTO) {
        LOG.debug("Request to save Prestation : {}", prestationDTO);
        Prestation prestation = prestationMapper.toEntity(prestationDTO);
        prestation = prestationRepository.save(prestation);
        return prestationMapper.toDto(prestation);
    }

    @Override
    public PrestationDTO update(PrestationDTO prestationDTO) {
        LOG.debug("Request to update Prestation : {}", prestationDTO);
        Prestation prestation = prestationMapper.toEntity(prestationDTO);
        prestation = prestationRepository.save(prestation);
        return prestationMapper.toDto(prestation);
    }

    @Override
    public Optional<PrestationDTO> partialUpdate(PrestationDTO prestationDTO) {
        LOG.debug("Request to partially update Prestation : {}", prestationDTO);

        return prestationRepository
            .findById(prestationDTO.getId())
            .map(existingPrestation -> {
                prestationMapper.partialUpdate(existingPrestation, prestationDTO);

                return existingPrestation;
            })
            .map(prestationRepository::save)
            .map(prestationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestationDTO> findAll() {
        LOG.debug("Request to get all Prestations");
        return prestationRepository.findAll().stream().map(prestationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<PrestationDTO> findAllWithEagerRelationships() {
        return prestationRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(prestationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrestationDTO> findOne(UUID id) {
        LOG.debug("Request to get Prestation : {}", id);
        return prestationRepository.findOneWithEagerRelationships(id).map(prestationMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Prestation : {}", id);
        prestationRepository.deleteById(id);
    }
}
