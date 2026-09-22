package sn.orange.conciergerie.service.impl;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.domain.IdempotencyKey;
import sn.orange.conciergerie.repository.IdempotencyKeyRepository;
import sn.orange.conciergerie.service.IdempotencyKeyService;
import sn.orange.conciergerie.service.dto.IdempotencyKeyDTO;
import sn.orange.conciergerie.service.mapper.IdempotencyKeyMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.IdempotencyKey}.
 */
@Service
@Transactional
public class IdempotencyKeyServiceImpl implements IdempotencyKeyService {

    private static final Logger LOG = LoggerFactory.getLogger(IdempotencyKeyServiceImpl.class);

    private final IdempotencyKeyRepository idempotencyKeyRepository;

    private final IdempotencyKeyMapper idempotencyKeyMapper;

    public IdempotencyKeyServiceImpl(IdempotencyKeyRepository idempotencyKeyRepository, IdempotencyKeyMapper idempotencyKeyMapper) {
        this.idempotencyKeyRepository = idempotencyKeyRepository;
        this.idempotencyKeyMapper = idempotencyKeyMapper;
    }

    @Override
    public IdempotencyKeyDTO save(IdempotencyKeyDTO idempotencyKeyDTO) {
        LOG.debug("Request to save IdempotencyKey : {}", idempotencyKeyDTO);
        IdempotencyKey idempotencyKey = idempotencyKeyMapper.toEntity(idempotencyKeyDTO);
        idempotencyKey = idempotencyKeyRepository.save(idempotencyKey);
        return idempotencyKeyMapper.toDto(idempotencyKey);
    }

    @Override
    public IdempotencyKeyDTO update(IdempotencyKeyDTO idempotencyKeyDTO) {
        LOG.debug("Request to update IdempotencyKey : {}", idempotencyKeyDTO);
        IdempotencyKey idempotencyKey = idempotencyKeyMapper.toEntity(idempotencyKeyDTO);
        idempotencyKey = idempotencyKeyRepository.save(idempotencyKey);
        return idempotencyKeyMapper.toDto(idempotencyKey);
    }

    @Override
    public Optional<IdempotencyKeyDTO> partialUpdate(IdempotencyKeyDTO idempotencyKeyDTO) {
        LOG.debug("Request to partially update IdempotencyKey : {}", idempotencyKeyDTO);

        return idempotencyKeyRepository
            .findById(idempotencyKeyDTO.getId())
            .map(existingIdempotencyKey -> {
                idempotencyKeyMapper.partialUpdate(existingIdempotencyKey, idempotencyKeyDTO);

                return existingIdempotencyKey;
            })
            .map(idempotencyKeyRepository::save)
            .map(idempotencyKeyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IdempotencyKeyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all IdempotencyKeys");
        return idempotencyKeyRepository.findAll(pageable).map(idempotencyKeyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IdempotencyKeyDTO> findOne(UUID id) {
        LOG.debug("Request to get IdempotencyKey : {}", id);
        return idempotencyKeyRepository.findById(id).map(idempotencyKeyMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete IdempotencyKey : {}", id);
        idempotencyKeyRepository.deleteById(id);
    }
}
