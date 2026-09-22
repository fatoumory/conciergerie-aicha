package sn.orange.conciergerie.service.impl;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.domain.Evaluation;
import sn.orange.conciergerie.repository.EvaluationRepository;
import sn.orange.conciergerie.service.EvaluationService;
import sn.orange.conciergerie.service.dto.EvaluationDTO;
import sn.orange.conciergerie.service.mapper.EvaluationMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.Evaluation}.
 */
@Service
@Transactional
public class EvaluationServiceImpl implements EvaluationService {

    private static final Logger LOG = LoggerFactory.getLogger(EvaluationServiceImpl.class);

    private final EvaluationRepository evaluationRepository;

    private final EvaluationMapper evaluationMapper;

    public EvaluationServiceImpl(EvaluationRepository evaluationRepository, EvaluationMapper evaluationMapper) {
        this.evaluationRepository = evaluationRepository;
        this.evaluationMapper = evaluationMapper;
    }

    @Override
    public EvaluationDTO save(EvaluationDTO evaluationDTO) {
        LOG.debug("Request to save Evaluation : {}", evaluationDTO);
        Evaluation evaluation = evaluationMapper.toEntity(evaluationDTO);
        evaluation = evaluationRepository.save(evaluation);
        return evaluationMapper.toDto(evaluation);
    }

    @Override
    public EvaluationDTO update(EvaluationDTO evaluationDTO) {
        LOG.debug("Request to update Evaluation : {}", evaluationDTO);
        Evaluation evaluation = evaluationMapper.toEntity(evaluationDTO);
        evaluation = evaluationRepository.save(evaluation);
        return evaluationMapper.toDto(evaluation);
    }

    @Override
    public Optional<EvaluationDTO> partialUpdate(EvaluationDTO evaluationDTO) {
        LOG.debug("Request to partially update Evaluation : {}", evaluationDTO);

        return evaluationRepository
            .findById(evaluationDTO.getId())
            .map(existingEvaluation -> {
                evaluationMapper.partialUpdate(existingEvaluation, evaluationDTO);

                return existingEvaluation;
            })
            .map(evaluationRepository::save)
            .map(evaluationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EvaluationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Evaluations");
        return evaluationRepository.findAll(pageable).map(evaluationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EvaluationDTO> findOne(UUID id) {
        LOG.debug("Request to get Evaluation : {}", id);
        return evaluationRepository.findById(id).map(evaluationMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Evaluation : {}", id);
        evaluationRepository.deleteById(id);
    }
}
