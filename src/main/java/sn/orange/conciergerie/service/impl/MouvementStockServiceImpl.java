package sn.orange.conciergerie.service.impl;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.domain.MouvementStock;
import sn.orange.conciergerie.repository.MouvementStockRepository;
import sn.orange.conciergerie.service.MouvementStockService;
import sn.orange.conciergerie.service.dto.MouvementStockDTO;
import sn.orange.conciergerie.service.mapper.MouvementStockMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.MouvementStock}.
 */
@Service
@Transactional
public class MouvementStockServiceImpl implements MouvementStockService {

    private static final Logger LOG = LoggerFactory.getLogger(MouvementStockServiceImpl.class);

    private final MouvementStockRepository mouvementStockRepository;

    private final MouvementStockMapper mouvementStockMapper;

    public MouvementStockServiceImpl(MouvementStockRepository mouvementStockRepository, MouvementStockMapper mouvementStockMapper) {
        this.mouvementStockRepository = mouvementStockRepository;
        this.mouvementStockMapper = mouvementStockMapper;
    }

    @Override
    public MouvementStockDTO save(MouvementStockDTO mouvementStockDTO) {
        LOG.debug("Request to save MouvementStock : {}", mouvementStockDTO);
        MouvementStock mouvementStock = mouvementStockMapper.toEntity(mouvementStockDTO);
        mouvementStock = mouvementStockRepository.save(mouvementStock);
        return mouvementStockMapper.toDto(mouvementStock);
    }

    @Override
    public MouvementStockDTO update(MouvementStockDTO mouvementStockDTO) {
        LOG.debug("Request to update MouvementStock : {}", mouvementStockDTO);
        MouvementStock mouvementStock = mouvementStockMapper.toEntity(mouvementStockDTO);
        mouvementStock = mouvementStockRepository.save(mouvementStock);
        return mouvementStockMapper.toDto(mouvementStock);
    }

    @Override
    public Optional<MouvementStockDTO> partialUpdate(MouvementStockDTO mouvementStockDTO) {
        LOG.debug("Request to partially update MouvementStock : {}", mouvementStockDTO);

        return mouvementStockRepository
            .findById(mouvementStockDTO.getId())
            .map(existingMouvementStock -> {
                mouvementStockMapper.partialUpdate(existingMouvementStock, mouvementStockDTO);

                return existingMouvementStock;
            })
            .map(mouvementStockRepository::save)
            .map(mouvementStockMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MouvementStockDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MouvementStocks");
        return mouvementStockRepository.findAll(pageable).map(mouvementStockMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MouvementStockDTO> findOne(UUID id) {
        LOG.debug("Request to get MouvementStock : {}", id);
        return mouvementStockRepository.findById(id).map(mouvementStockMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete MouvementStock : {}", id);
        mouvementStockRepository.deleteById(id);
    }
}
