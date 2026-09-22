package sn.orange.conciergerie.service.impl;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.domain.HistoriqueStatutDemande;
import sn.orange.conciergerie.repository.HistoriqueStatutDemandeRepository;
import sn.orange.conciergerie.service.HistoriqueStatutDemandeService;
import sn.orange.conciergerie.service.dto.HistoriqueStatutDemandeDTO;
import sn.orange.conciergerie.service.mapper.HistoriqueStatutDemandeMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.HistoriqueStatutDemande}.
 */
@Service
@Transactional
public class HistoriqueStatutDemandeServiceImpl implements HistoriqueStatutDemandeService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueStatutDemandeServiceImpl.class);

    private final HistoriqueStatutDemandeRepository historiqueStatutDemandeRepository;

    private final HistoriqueStatutDemandeMapper historiqueStatutDemandeMapper;

    public HistoriqueStatutDemandeServiceImpl(
        HistoriqueStatutDemandeRepository historiqueStatutDemandeRepository,
        HistoriqueStatutDemandeMapper historiqueStatutDemandeMapper
    ) {
        this.historiqueStatutDemandeRepository = historiqueStatutDemandeRepository;
        this.historiqueStatutDemandeMapper = historiqueStatutDemandeMapper;
    }

    @Override
    public HistoriqueStatutDemandeDTO save(HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO) {
        LOG.debug("Request to save HistoriqueStatutDemande : {}", historiqueStatutDemandeDTO);
        HistoriqueStatutDemande historiqueStatutDemande = historiqueStatutDemandeMapper.toEntity(historiqueStatutDemandeDTO);
        historiqueStatutDemande = historiqueStatutDemandeRepository.save(historiqueStatutDemande);
        return historiqueStatutDemandeMapper.toDto(historiqueStatutDemande);
    }

    @Override
    public HistoriqueStatutDemandeDTO update(HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO) {
        LOG.debug("Request to update HistoriqueStatutDemande : {}", historiqueStatutDemandeDTO);
        HistoriqueStatutDemande historiqueStatutDemande = historiqueStatutDemandeMapper.toEntity(historiqueStatutDemandeDTO);
        historiqueStatutDemande = historiqueStatutDemandeRepository.save(historiqueStatutDemande);
        return historiqueStatutDemandeMapper.toDto(historiqueStatutDemande);
    }

    @Override
    public Optional<HistoriqueStatutDemandeDTO> partialUpdate(HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO) {
        LOG.debug("Request to partially update HistoriqueStatutDemande : {}", historiqueStatutDemandeDTO);

        return historiqueStatutDemandeRepository
            .findById(historiqueStatutDemandeDTO.getId())
            .map(existingHistoriqueStatutDemande -> {
                historiqueStatutDemandeMapper.partialUpdate(existingHistoriqueStatutDemande, historiqueStatutDemandeDTO);

                return existingHistoriqueStatutDemande;
            })
            .map(historiqueStatutDemandeRepository::save)
            .map(historiqueStatutDemandeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueStatutDemandeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HistoriqueStatutDemandes");
        return historiqueStatutDemandeRepository.findAll(pageable).map(historiqueStatutDemandeMapper::toDto);
    }

    public Page<HistoriqueStatutDemandeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return historiqueStatutDemandeRepository.findAllWithEagerRelationships(pageable).map(historiqueStatutDemandeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriqueStatutDemandeDTO> findOne(UUID id) {
        LOG.debug("Request to get HistoriqueStatutDemande : {}", id);
        return historiqueStatutDemandeRepository.findOneWithEagerRelationships(id).map(historiqueStatutDemandeMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete HistoriqueStatutDemande : {}", id);
        historiqueStatutDemandeRepository.deleteById(id);
    }
}
