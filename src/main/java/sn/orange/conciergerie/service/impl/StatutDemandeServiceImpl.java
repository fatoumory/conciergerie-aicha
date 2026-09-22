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
import sn.orange.conciergerie.domain.StatutDemande;
import sn.orange.conciergerie.repository.StatutDemandeRepository;
import sn.orange.conciergerie.service.StatutDemandeService;
import sn.orange.conciergerie.service.dto.StatutDemandeDTO;
import sn.orange.conciergerie.service.mapper.StatutDemandeMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.StatutDemande}.
 */
@Service
@Transactional
public class StatutDemandeServiceImpl implements StatutDemandeService {

    private static final Logger LOG = LoggerFactory.getLogger(StatutDemandeServiceImpl.class);

    private final StatutDemandeRepository statutDemandeRepository;

    private final StatutDemandeMapper statutDemandeMapper;

    public StatutDemandeServiceImpl(StatutDemandeRepository statutDemandeRepository, StatutDemandeMapper statutDemandeMapper) {
        this.statutDemandeRepository = statutDemandeRepository;
        this.statutDemandeMapper = statutDemandeMapper;
    }

    @Override
    public StatutDemandeDTO save(StatutDemandeDTO statutDemandeDTO) {
        LOG.debug("Request to save StatutDemande : {}", statutDemandeDTO);
        StatutDemande statutDemande = statutDemandeMapper.toEntity(statutDemandeDTO);
        statutDemande = statutDemandeRepository.save(statutDemande);
        return statutDemandeMapper.toDto(statutDemande);
    }

    @Override
    public StatutDemandeDTO update(StatutDemandeDTO statutDemandeDTO) {
        LOG.debug("Request to update StatutDemande : {}", statutDemandeDTO);
        StatutDemande statutDemande = statutDemandeMapper.toEntity(statutDemandeDTO);
        statutDemande = statutDemandeRepository.save(statutDemande);
        return statutDemandeMapper.toDto(statutDemande);
    }

    @Override
    public Optional<StatutDemandeDTO> partialUpdate(StatutDemandeDTO statutDemandeDTO) {
        LOG.debug("Request to partially update StatutDemande : {}", statutDemandeDTO);

        return statutDemandeRepository
            .findById(statutDemandeDTO.getId())
            .map(existingStatutDemande -> {
                statutDemandeMapper.partialUpdate(existingStatutDemande, statutDemandeDTO);

                return existingStatutDemande;
            })
            .map(statutDemandeRepository::save)
            .map(statutDemandeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatutDemandeDTO> findAll() {
        LOG.debug("Request to get all StatutDemandes");
        return statutDemandeRepository.findAll().stream().map(statutDemandeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StatutDemandeDTO> findOne(UUID id) {
        LOG.debug("Request to get StatutDemande : {}", id);
        return statutDemandeRepository.findById(id).map(statutDemandeMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete StatutDemande : {}", id);
        statutDemandeRepository.deleteById(id);
    }
}
