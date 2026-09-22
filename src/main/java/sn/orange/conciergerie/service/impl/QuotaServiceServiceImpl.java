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
import sn.orange.conciergerie.domain.QuotaService;
import sn.orange.conciergerie.repository.QuotaServiceRepository;
import sn.orange.conciergerie.service.QuotaServiceService;
import sn.orange.conciergerie.service.dto.QuotaServiceDTO;
import sn.orange.conciergerie.service.mapper.QuotaServiceMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.QuotaService}.
 */
@Service
@Transactional
public class QuotaServiceServiceImpl implements QuotaServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(QuotaServiceServiceImpl.class);

    private final QuotaServiceRepository quotaServiceRepository;

    private final QuotaServiceMapper quotaServiceMapper;

    public QuotaServiceServiceImpl(QuotaServiceRepository quotaServiceRepository, QuotaServiceMapper quotaServiceMapper) {
        this.quotaServiceRepository = quotaServiceRepository;
        this.quotaServiceMapper = quotaServiceMapper;
    }

    @Override
    public QuotaServiceDTO save(QuotaServiceDTO quotaServiceDTO) {
        LOG.debug("Request to save QuotaService : {}", quotaServiceDTO);
        QuotaService quotaService = quotaServiceMapper.toEntity(quotaServiceDTO);
        quotaService = quotaServiceRepository.save(quotaService);
        return quotaServiceMapper.toDto(quotaService);
    }

    @Override
    public QuotaServiceDTO update(QuotaServiceDTO quotaServiceDTO) {
        LOG.debug("Request to update QuotaService : {}", quotaServiceDTO);
        QuotaService quotaService = quotaServiceMapper.toEntity(quotaServiceDTO);
        quotaService = quotaServiceRepository.save(quotaService);
        return quotaServiceMapper.toDto(quotaService);
    }

    @Override
    public Optional<QuotaServiceDTO> partialUpdate(QuotaServiceDTO quotaServiceDTO) {
        LOG.debug("Request to partially update QuotaService : {}", quotaServiceDTO);

        return quotaServiceRepository
            .findById(quotaServiceDTO.getId())
            .map(existingQuotaService -> {
                quotaServiceMapper.partialUpdate(existingQuotaService, quotaServiceDTO);

                return existingQuotaService;
            })
            .map(quotaServiceRepository::save)
            .map(quotaServiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotaServiceDTO> findAll() {
        LOG.debug("Request to get all QuotaServices");
        return quotaServiceRepository.findAll().stream().map(quotaServiceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuotaServiceDTO> findOne(UUID id) {
        LOG.debug("Request to get QuotaService : {}", id);
        return quotaServiceRepository.findById(id).map(quotaServiceMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete QuotaService : {}", id);
        quotaServiceRepository.deleteById(id);
    }
}
