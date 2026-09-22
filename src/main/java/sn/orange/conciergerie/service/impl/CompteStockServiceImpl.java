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
import sn.orange.conciergerie.domain.CompteStock;
import sn.orange.conciergerie.repository.CompteStockRepository;
import sn.orange.conciergerie.service.CompteStockService;
import sn.orange.conciergerie.service.dto.CompteStockDTO;
import sn.orange.conciergerie.service.mapper.CompteStockMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.CompteStock}.
 */
@Service
@Transactional
public class CompteStockServiceImpl implements CompteStockService {

    private static final Logger LOG = LoggerFactory.getLogger(CompteStockServiceImpl.class);

    private final CompteStockRepository compteStockRepository;

    private final CompteStockMapper compteStockMapper;

    public CompteStockServiceImpl(CompteStockRepository compteStockRepository, CompteStockMapper compteStockMapper) {
        this.compteStockRepository = compteStockRepository;
        this.compteStockMapper = compteStockMapper;
    }

    @Override
    public CompteStockDTO save(CompteStockDTO compteStockDTO) {
        LOG.debug("Request to save CompteStock : {}", compteStockDTO);
        CompteStock compteStock = compteStockMapper.toEntity(compteStockDTO);
        compteStock = compteStockRepository.save(compteStock);
        return compteStockMapper.toDto(compteStock);
    }

    @Override
    public CompteStockDTO update(CompteStockDTO compteStockDTO) {
        LOG.debug("Request to update CompteStock : {}", compteStockDTO);
        CompteStock compteStock = compteStockMapper.toEntity(compteStockDTO);
        compteStock = compteStockRepository.save(compteStock);
        return compteStockMapper.toDto(compteStock);
    }

    @Override
    public Optional<CompteStockDTO> partialUpdate(CompteStockDTO compteStockDTO) {
        LOG.debug("Request to partially update CompteStock : {}", compteStockDTO);

        return compteStockRepository
            .findById(compteStockDTO.getId())
            .map(existingCompteStock -> {
                compteStockMapper.partialUpdate(existingCompteStock, compteStockDTO);

                return existingCompteStock;
            })
            .map(compteStockRepository::save)
            .map(compteStockMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompteStockDTO> findAll() {
        LOG.debug("Request to get all CompteStocks");
        return compteStockRepository.findAll().stream().map(compteStockMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<CompteStockDTO> findAllWithEagerRelationships() {
        return compteStockRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(compteStockMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompteStockDTO> findOne(UUID id) {
        LOG.debug("Request to get CompteStock : {}", id);
        return compteStockRepository.findOneWithEagerRelationships(id).map(compteStockMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete CompteStock : {}", id);
        compteStockRepository.deleteById(id);
    }
}
