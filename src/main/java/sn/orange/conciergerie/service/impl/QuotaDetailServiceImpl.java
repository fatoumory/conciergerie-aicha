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
import sn.orange.conciergerie.domain.QuotaDetail;
import sn.orange.conciergerie.repository.QuotaDetailRepository;
import sn.orange.conciergerie.service.QuotaDetailService;
import sn.orange.conciergerie.service.dto.QuotaDetailDTO;
import sn.orange.conciergerie.service.mapper.QuotaDetailMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.QuotaDetail}.
 */
@Service
@Transactional
public class QuotaDetailServiceImpl implements QuotaDetailService {

    private static final Logger LOG = LoggerFactory.getLogger(QuotaDetailServiceImpl.class);

    private final QuotaDetailRepository quotaDetailRepository;

    private final QuotaDetailMapper quotaDetailMapper;

    public QuotaDetailServiceImpl(QuotaDetailRepository quotaDetailRepository, QuotaDetailMapper quotaDetailMapper) {
        this.quotaDetailRepository = quotaDetailRepository;
        this.quotaDetailMapper = quotaDetailMapper;
    }

    @Override
    public QuotaDetailDTO save(QuotaDetailDTO quotaDetailDTO) {
        LOG.debug("Request to save QuotaDetail : {}", quotaDetailDTO);
        QuotaDetail quotaDetail = quotaDetailMapper.toEntity(quotaDetailDTO);
        quotaDetail = quotaDetailRepository.save(quotaDetail);
        return quotaDetailMapper.toDto(quotaDetail);
    }

    @Override
    public QuotaDetailDTO update(QuotaDetailDTO quotaDetailDTO) {
        LOG.debug("Request to update QuotaDetail : {}", quotaDetailDTO);
        QuotaDetail quotaDetail = quotaDetailMapper.toEntity(quotaDetailDTO);
        quotaDetail = quotaDetailRepository.save(quotaDetail);
        return quotaDetailMapper.toDto(quotaDetail);
    }

    @Override
    public Optional<QuotaDetailDTO> partialUpdate(QuotaDetailDTO quotaDetailDTO) {
        LOG.debug("Request to partially update QuotaDetail : {}", quotaDetailDTO);

        return quotaDetailRepository
            .findById(quotaDetailDTO.getId())
            .map(existingQuotaDetail -> {
                quotaDetailMapper.partialUpdate(existingQuotaDetail, quotaDetailDTO);

                return existingQuotaDetail;
            })
            .map(quotaDetailRepository::save)
            .map(quotaDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotaDetailDTO> findAll() {
        LOG.debug("Request to get all QuotaDetails");
        return quotaDetailRepository.findAll().stream().map(quotaDetailMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<QuotaDetailDTO> findAllWithEagerRelationships() {
        return quotaDetailRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(quotaDetailMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuotaDetailDTO> findOne(UUID id) {
        LOG.debug("Request to get QuotaDetail : {}", id);
        return quotaDetailRepository.findOneWithEagerRelationships(id).map(quotaDetailMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete QuotaDetail : {}", id);
        quotaDetailRepository.deleteById(id);
    }
}
