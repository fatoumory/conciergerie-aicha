package sn.orange.conciergerie.service.impl;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.domain.ConsommationQuota;
import sn.orange.conciergerie.repository.ConsommationQuotaRepository;
import sn.orange.conciergerie.service.ConsommationQuotaService;
import sn.orange.conciergerie.service.dto.ConsommationQuotaDTO;
import sn.orange.conciergerie.service.mapper.ConsommationQuotaMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.ConsommationQuota}.
 */
@Service
@Transactional
public class ConsommationQuotaServiceImpl implements ConsommationQuotaService {

    private static final Logger LOG = LoggerFactory.getLogger(ConsommationQuotaServiceImpl.class);

    private final ConsommationQuotaRepository consommationQuotaRepository;

    private final ConsommationQuotaMapper consommationQuotaMapper;

    public ConsommationQuotaServiceImpl(
        ConsommationQuotaRepository consommationQuotaRepository,
        ConsommationQuotaMapper consommationQuotaMapper
    ) {
        this.consommationQuotaRepository = consommationQuotaRepository;
        this.consommationQuotaMapper = consommationQuotaMapper;
    }

    @Override
    public ConsommationQuotaDTO save(ConsommationQuotaDTO consommationQuotaDTO) {
        LOG.debug("Request to save ConsommationQuota : {}", consommationQuotaDTO);
        ConsommationQuota consommationQuota = consommationQuotaMapper.toEntity(consommationQuotaDTO);
        consommationQuota = consommationQuotaRepository.save(consommationQuota);
        return consommationQuotaMapper.toDto(consommationQuota);
    }

    @Override
    public ConsommationQuotaDTO update(ConsommationQuotaDTO consommationQuotaDTO) {
        LOG.debug("Request to update ConsommationQuota : {}", consommationQuotaDTO);
        ConsommationQuota consommationQuota = consommationQuotaMapper.toEntity(consommationQuotaDTO);
        consommationQuota = consommationQuotaRepository.save(consommationQuota);
        return consommationQuotaMapper.toDto(consommationQuota);
    }

    @Override
    public Optional<ConsommationQuotaDTO> partialUpdate(ConsommationQuotaDTO consommationQuotaDTO) {
        LOG.debug("Request to partially update ConsommationQuota : {}", consommationQuotaDTO);

        return consommationQuotaRepository
            .findById(consommationQuotaDTO.getId())
            .map(existingConsommationQuota -> {
                consommationQuotaMapper.partialUpdate(existingConsommationQuota, consommationQuotaDTO);

                return existingConsommationQuota;
            })
            .map(consommationQuotaRepository::save)
            .map(consommationQuotaMapper::toDto);
    }

    public Page<ConsommationQuotaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return consommationQuotaRepository.findAllWithEagerRelationships(pageable).map(consommationQuotaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConsommationQuotaDTO> findOne(UUID id) {
        LOG.debug("Request to get ConsommationQuota : {}", id);
        return consommationQuotaRepository.findOneWithEagerRelationships(id).map(consommationQuotaMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete ConsommationQuota : {}", id);
        consommationQuotaRepository.deleteById(id);
    }
}
