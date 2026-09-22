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
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.repository.PartenaireRepository;
import sn.orange.conciergerie.service.PartenaireService;
import sn.orange.conciergerie.service.dto.PartenaireDTO;
import sn.orange.conciergerie.service.mapper.PartenaireMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.Partenaire}.
 */
@Service
@Transactional
public class PartenaireServiceImpl implements PartenaireService {

    private static final Logger LOG = LoggerFactory.getLogger(PartenaireServiceImpl.class);

    private final PartenaireRepository partenaireRepository;

    private final PartenaireMapper partenaireMapper;

    public PartenaireServiceImpl(PartenaireRepository partenaireRepository, PartenaireMapper partenaireMapper) {
        this.partenaireRepository = partenaireRepository;
        this.partenaireMapper = partenaireMapper;
    }

    @Override
    public PartenaireDTO save(PartenaireDTO partenaireDTO) {
        LOG.debug("Request to save Partenaire : {}", partenaireDTO);
        Partenaire partenaire = partenaireMapper.toEntity(partenaireDTO);
        partenaire = partenaireRepository.save(partenaire);
        return partenaireMapper.toDto(partenaire);
    }

    @Override
    public PartenaireDTO update(PartenaireDTO partenaireDTO) {
        LOG.debug("Request to update Partenaire : {}", partenaireDTO);
        Partenaire partenaire = partenaireMapper.toEntity(partenaireDTO);
        partenaire = partenaireRepository.save(partenaire);
        return partenaireMapper.toDto(partenaire);
    }

    @Override
    public Optional<PartenaireDTO> partialUpdate(PartenaireDTO partenaireDTO) {
        LOG.debug("Request to partially update Partenaire : {}", partenaireDTO);

        return partenaireRepository
            .findById(partenaireDTO.getId())
            .map(existingPartenaire -> {
                partenaireMapper.partialUpdate(existingPartenaire, partenaireDTO);

                return existingPartenaire;
            })
            .map(partenaireRepository::save)
            .map(partenaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartenaireDTO> findAll() {
        LOG.debug("Request to get all Partenaires");
        return partenaireRepository.findAll().stream().map(partenaireMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PartenaireDTO> findOne(UUID id) {
        LOG.debug("Request to get Partenaire : {}", id);
        return partenaireRepository.findById(id).map(partenaireMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Partenaire : {}", id);
        partenaireRepository.deleteById(id);
    }
}
