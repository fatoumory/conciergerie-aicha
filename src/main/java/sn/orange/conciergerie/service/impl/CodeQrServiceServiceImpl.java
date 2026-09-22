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
import sn.orange.conciergerie.domain.CodeQrService;
import sn.orange.conciergerie.repository.CodeQrServiceRepository;
import sn.orange.conciergerie.service.CodeQrServiceService;
import sn.orange.conciergerie.service.dto.CodeQrServiceDTO;
import sn.orange.conciergerie.service.mapper.CodeQrServiceMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.CodeQrService}.
 */
@Service
@Transactional
public class CodeQrServiceServiceImpl implements CodeQrServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(CodeQrServiceServiceImpl.class);

    private final CodeQrServiceRepository codeQrServiceRepository;

    private final CodeQrServiceMapper codeQrServiceMapper;

    public CodeQrServiceServiceImpl(CodeQrServiceRepository codeQrServiceRepository, CodeQrServiceMapper codeQrServiceMapper) {
        this.codeQrServiceRepository = codeQrServiceRepository;
        this.codeQrServiceMapper = codeQrServiceMapper;
    }

    @Override
    public CodeQrServiceDTO save(CodeQrServiceDTO codeQrServiceDTO) {
        LOG.debug("Request to save CodeQrService : {}", codeQrServiceDTO);
        CodeQrService codeQrService = codeQrServiceMapper.toEntity(codeQrServiceDTO);
        codeQrService = codeQrServiceRepository.save(codeQrService);
        return codeQrServiceMapper.toDto(codeQrService);
    }

    @Override
    public CodeQrServiceDTO update(CodeQrServiceDTO codeQrServiceDTO) {
        LOG.debug("Request to update CodeQrService : {}", codeQrServiceDTO);
        CodeQrService codeQrService = codeQrServiceMapper.toEntity(codeQrServiceDTO);
        codeQrService = codeQrServiceRepository.save(codeQrService);
        return codeQrServiceMapper.toDto(codeQrService);
    }

    @Override
    public Optional<CodeQrServiceDTO> partialUpdate(CodeQrServiceDTO codeQrServiceDTO) {
        LOG.debug("Request to partially update CodeQrService : {}", codeQrServiceDTO);

        return codeQrServiceRepository
            .findById(codeQrServiceDTO.getId())
            .map(existingCodeQrService -> {
                codeQrServiceMapper.partialUpdate(existingCodeQrService, codeQrServiceDTO);

                return existingCodeQrService;
            })
            .map(codeQrServiceRepository::save)
            .map(codeQrServiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodeQrServiceDTO> findAll() {
        LOG.debug("Request to get all CodeQrServices");
        return codeQrServiceRepository.findAll().stream().map(codeQrServiceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CodeQrServiceDTO> findOne(UUID id) {
        LOG.debug("Request to get CodeQrService : {}", id);
        return codeQrServiceRepository.findById(id).map(codeQrServiceMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete CodeQrService : {}", id);
        codeQrServiceRepository.deleteById(id);
    }
}
