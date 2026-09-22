package sn.orange.conciergerie.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.repository.DemandeRepository;
import sn.orange.conciergerie.service.DemandeService;
import sn.orange.conciergerie.service.dto.DemandeDTO;
import sn.orange.conciergerie.service.mapper.DemandeMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.Demande}.
 */
@Service
@Transactional
public class DemandeServiceImpl implements DemandeService {

    private static final Logger LOG = LoggerFactory.getLogger(DemandeServiceImpl.class);

    private final DemandeRepository demandeRepository;

    private final DemandeMapper demandeMapper;

    public DemandeServiceImpl(DemandeRepository demandeRepository, DemandeMapper demandeMapper) {
        this.demandeRepository = demandeRepository;
        this.demandeMapper = demandeMapper;
    }

    @Override
    public DemandeDTO save(DemandeDTO demandeDTO) {
        LOG.debug("Request to save Demande : {}", demandeDTO);
        Demande demande = demandeMapper.toEntity(demandeDTO);
        demande = demandeRepository.save(demande);
        return demandeMapper.toDto(demande);
    }

    @Override
    public DemandeDTO update(DemandeDTO demandeDTO) {
        LOG.debug("Request to update Demande : {}", demandeDTO);
        Demande demande = demandeMapper.toEntity(demandeDTO);
        demande = demandeRepository.save(demande);
        return demandeMapper.toDto(demande);
    }

    @Override
    public Optional<DemandeDTO> partialUpdate(DemandeDTO demandeDTO) {
        LOG.debug("Request to partially update Demande : {}", demandeDTO);

        return demandeRepository
            .findById(demandeDTO.getId())
            .map(existingDemande -> {
                demandeMapper.partialUpdate(existingDemande, demandeDTO);

                return existingDemande;
            })
            .map(demandeRepository::save)
            .map(demandeMapper::toDto);
    }

    public Page<DemandeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return demandeRepository.findAllWithEagerRelationships(pageable).map(demandeMapper::toDto);
    }

    /**
     *  Get all the demandes where Prestation is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DemandeDTO> findAllWherePrestationIsNull() {
        LOG.debug("Request to get all demandes where Prestation is null");
        return StreamSupport.stream(demandeRepository.findAll().spliterator(), false)
            .filter(demande -> demande.getPrestation() == null)
            .map(demandeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the demandes where CodeQrService is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DemandeDTO> findAllWhereCodeQrServiceIsNull() {
        LOG.debug("Request to get all demandes where CodeQrService is null");
        return StreamSupport.stream(demandeRepository.findAll().spliterator(), false)
            .filter(demande -> demande.getCodeQrService() == null)
            .map(demandeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the demandes where Facture is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DemandeDTO> findAllWhereFactureIsNull() {
        LOG.debug("Request to get all demandes where Facture is null");
        return StreamSupport.stream(demandeRepository.findAll().spliterator(), false)
            .filter(demande -> demande.getFacture() == null)
            .map(demandeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the demandes where Evaluation is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DemandeDTO> findAllWhereEvaluationIsNull() {
        LOG.debug("Request to get all demandes where Evaluation is null");
        return StreamSupport.stream(demandeRepository.findAll().spliterator(), false)
            .filter(demande -> demande.getEvaluation() == null)
            .map(demandeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DemandeDTO> findOne(UUID id) {
        LOG.debug("Request to get Demande : {}", id);
        return demandeRepository.findOneWithEagerRelationships(id).map(demandeMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Demande : {}", id);
        demandeRepository.deleteById(id);
    }
}
