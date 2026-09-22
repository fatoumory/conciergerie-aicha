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
import sn.orange.conciergerie.domain.CodePromo;
import sn.orange.conciergerie.repository.CodePromoRepository;
import sn.orange.conciergerie.service.CodePromoService;
import sn.orange.conciergerie.service.dto.CodePromoDTO;
import sn.orange.conciergerie.service.mapper.CodePromoMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.CodePromo}.
 */
@Service
@Transactional
public class CodePromoServiceImpl implements CodePromoService {

    private static final Logger LOG = LoggerFactory.getLogger(CodePromoServiceImpl.class);

    private final CodePromoRepository codePromoRepository;

    private final CodePromoMapper codePromoMapper;

    public CodePromoServiceImpl(CodePromoRepository codePromoRepository, CodePromoMapper codePromoMapper) {
        this.codePromoRepository = codePromoRepository;
        this.codePromoMapper = codePromoMapper;
    }

    @Override
    public CodePromoDTO save(CodePromoDTO codePromoDTO) {
        LOG.debug("Request to save CodePromo : {}", codePromoDTO);
        CodePromo codePromo = codePromoMapper.toEntity(codePromoDTO);
        codePromo = codePromoRepository.save(codePromo);
        return codePromoMapper.toDto(codePromo);
    }

    @Override
    public CodePromoDTO update(CodePromoDTO codePromoDTO) {
        LOG.debug("Request to update CodePromo : {}", codePromoDTO);
        CodePromo codePromo = codePromoMapper.toEntity(codePromoDTO);
        codePromo = codePromoRepository.save(codePromo);
        return codePromoMapper.toDto(codePromo);
    }

    @Override
    public Optional<CodePromoDTO> partialUpdate(CodePromoDTO codePromoDTO) {
        LOG.debug("Request to partially update CodePromo : {}", codePromoDTO);

        return codePromoRepository
            .findById(codePromoDTO.getId())
            .map(existingCodePromo -> {
                codePromoMapper.partialUpdate(existingCodePromo, codePromoDTO);

                return existingCodePromo;
            })
            .map(codePromoRepository::save)
            .map(codePromoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodePromoDTO> findAll() {
        LOG.debug("Request to get all CodePromos");
        return codePromoRepository.findAll().stream().map(codePromoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CodePromoDTO> findOne(UUID id) {
        LOG.debug("Request to get CodePromo : {}", id);
        return codePromoRepository.findById(id).map(codePromoMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete CodePromo : {}", id);
        codePromoRepository.deleteById(id);
    }
}
