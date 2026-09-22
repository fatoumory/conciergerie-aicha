package sn.orange.conciergerie.service.impl;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.domain.UtilisationCodeQr;
import sn.orange.conciergerie.repository.UtilisationCodeQrRepository;
import sn.orange.conciergerie.service.UtilisationCodeQrService;
import sn.orange.conciergerie.service.dto.UtilisationCodeQrDTO;
import sn.orange.conciergerie.service.mapper.UtilisationCodeQrMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.UtilisationCodeQr}.
 */
@Service
@Transactional
public class UtilisationCodeQrServiceImpl implements UtilisationCodeQrService {

    private static final Logger LOG = LoggerFactory.getLogger(UtilisationCodeQrServiceImpl.class);

    private final UtilisationCodeQrRepository utilisationCodeQrRepository;

    private final UtilisationCodeQrMapper utilisationCodeQrMapper;

    public UtilisationCodeQrServiceImpl(
        UtilisationCodeQrRepository utilisationCodeQrRepository,
        UtilisationCodeQrMapper utilisationCodeQrMapper
    ) {
        this.utilisationCodeQrRepository = utilisationCodeQrRepository;
        this.utilisationCodeQrMapper = utilisationCodeQrMapper;
    }

    @Override
    public UtilisationCodeQrDTO save(UtilisationCodeQrDTO utilisationCodeQrDTO) {
        LOG.debug("Request to save UtilisationCodeQr : {}", utilisationCodeQrDTO);
        UtilisationCodeQr utilisationCodeQr = utilisationCodeQrMapper.toEntity(utilisationCodeQrDTO);
        utilisationCodeQr = utilisationCodeQrRepository.save(utilisationCodeQr);
        return utilisationCodeQrMapper.toDto(utilisationCodeQr);
    }

    @Override
    public UtilisationCodeQrDTO update(UtilisationCodeQrDTO utilisationCodeQrDTO) {
        LOG.debug("Request to update UtilisationCodeQr : {}", utilisationCodeQrDTO);
        UtilisationCodeQr utilisationCodeQr = utilisationCodeQrMapper.toEntity(utilisationCodeQrDTO);
        utilisationCodeQr = utilisationCodeQrRepository.save(utilisationCodeQr);
        return utilisationCodeQrMapper.toDto(utilisationCodeQr);
    }

    @Override
    public Optional<UtilisationCodeQrDTO> partialUpdate(UtilisationCodeQrDTO utilisationCodeQrDTO) {
        LOG.debug("Request to partially update UtilisationCodeQr : {}", utilisationCodeQrDTO);

        return utilisationCodeQrRepository
            .findById(utilisationCodeQrDTO.getId())
            .map(existingUtilisationCodeQr -> {
                utilisationCodeQrMapper.partialUpdate(existingUtilisationCodeQr, utilisationCodeQrDTO);

                return existingUtilisationCodeQr;
            })
            .map(utilisationCodeQrRepository::save)
            .map(utilisationCodeQrMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UtilisationCodeQrDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UtilisationCodeQrs");
        return utilisationCodeQrRepository.findAll(pageable).map(utilisationCodeQrMapper::toDto);
    }

    public Page<UtilisationCodeQrDTO> findAllWithEagerRelationships(Pageable pageable) {
        return utilisationCodeQrRepository.findAllWithEagerRelationships(pageable).map(utilisationCodeQrMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UtilisationCodeQrDTO> findOne(UUID id) {
        LOG.debug("Request to get UtilisationCodeQr : {}", id);
        return utilisationCodeQrRepository.findOneWithEagerRelationships(id).map(utilisationCodeQrMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete UtilisationCodeQr : {}", id);
        utilisationCodeQrRepository.deleteById(id);
    }
}
