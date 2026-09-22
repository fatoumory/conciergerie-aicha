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
import sn.orange.conciergerie.domain.AffectationDemande;
import sn.orange.conciergerie.repository.AffectationDemandeRepository;
import sn.orange.conciergerie.service.AffectationDemandeService;
import sn.orange.conciergerie.service.dto.AffectationDemandeDTO;
import sn.orange.conciergerie.service.mapper.AffectationDemandeMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.AffectationDemande}.
 */
@Service
@Transactional
public class AffectationDemandeServiceImpl implements AffectationDemandeService {

    private static final Logger LOG = LoggerFactory.getLogger(AffectationDemandeServiceImpl.class);

    private final AffectationDemandeRepository affectationDemandeRepository;

    private final AffectationDemandeMapper affectationDemandeMapper;

    public AffectationDemandeServiceImpl(
        AffectationDemandeRepository affectationDemandeRepository,
        AffectationDemandeMapper affectationDemandeMapper
    ) {
        this.affectationDemandeRepository = affectationDemandeRepository;
        this.affectationDemandeMapper = affectationDemandeMapper;
    }

    @Override
    public AffectationDemandeDTO save(AffectationDemandeDTO affectationDemandeDTO) {
        LOG.debug("Request to save AffectationDemande : {}", affectationDemandeDTO);
        AffectationDemande affectationDemande = affectationDemandeMapper.toEntity(affectationDemandeDTO);
        affectationDemande = affectationDemandeRepository.save(affectationDemande);
        return affectationDemandeMapper.toDto(affectationDemande);
    }

    @Override
    public AffectationDemandeDTO update(AffectationDemandeDTO affectationDemandeDTO) {
        LOG.debug("Request to update AffectationDemande : {}", affectationDemandeDTO);
        AffectationDemande affectationDemande = affectationDemandeMapper.toEntity(affectationDemandeDTO);
        affectationDemande = affectationDemandeRepository.save(affectationDemande);
        return affectationDemandeMapper.toDto(affectationDemande);
    }

    @Override
    public Optional<AffectationDemandeDTO> partialUpdate(AffectationDemandeDTO affectationDemandeDTO) {
        LOG.debug("Request to partially update AffectationDemande : {}", affectationDemandeDTO);

        return affectationDemandeRepository
            .findById(affectationDemandeDTO.getId())
            .map(existingAffectationDemande -> {
                affectationDemandeMapper.partialUpdate(existingAffectationDemande, affectationDemandeDTO);

                return existingAffectationDemande;
            })
            .map(affectationDemandeRepository::save)
            .map(affectationDemandeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AffectationDemandeDTO> findAll() {
        LOG.debug("Request to get all AffectationDemandes");
        return affectationDemandeRepository
            .findAll()
            .stream()
            .map(affectationDemandeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<AffectationDemandeDTO> findAllWithEagerRelationships() {
        return affectationDemandeRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(affectationDemandeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AffectationDemandeDTO> findOne(UUID id) {
        LOG.debug("Request to get AffectationDemande : {}", id);
        return affectationDemandeRepository.findOneWithEagerRelationships(id).map(affectationDemandeMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete AffectationDemande : {}", id);
        affectationDemandeRepository.deleteById(id);
    }
}
