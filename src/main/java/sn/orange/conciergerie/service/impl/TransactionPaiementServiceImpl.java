package sn.orange.conciergerie.service.impl;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.domain.TransactionPaiement;
import sn.orange.conciergerie.repository.TransactionPaiementRepository;
import sn.orange.conciergerie.service.TransactionPaiementService;
import sn.orange.conciergerie.service.dto.TransactionPaiementDTO;
import sn.orange.conciergerie.service.mapper.TransactionPaiementMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.TransactionPaiement}.
 */
@Service
@Transactional
public class TransactionPaiementServiceImpl implements TransactionPaiementService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionPaiementServiceImpl.class);

    private final TransactionPaiementRepository transactionPaiementRepository;

    private final TransactionPaiementMapper transactionPaiementMapper;

    public TransactionPaiementServiceImpl(
        TransactionPaiementRepository transactionPaiementRepository,
        TransactionPaiementMapper transactionPaiementMapper
    ) {
        this.transactionPaiementRepository = transactionPaiementRepository;
        this.transactionPaiementMapper = transactionPaiementMapper;
    }

    @Override
    public TransactionPaiementDTO save(TransactionPaiementDTO transactionPaiementDTO) {
        LOG.debug("Request to save TransactionPaiement : {}", transactionPaiementDTO);
        TransactionPaiement transactionPaiement = transactionPaiementMapper.toEntity(transactionPaiementDTO);
        transactionPaiement = transactionPaiementRepository.save(transactionPaiement);
        return transactionPaiementMapper.toDto(transactionPaiement);
    }

    @Override
    public TransactionPaiementDTO update(TransactionPaiementDTO transactionPaiementDTO) {
        LOG.debug("Request to update TransactionPaiement : {}", transactionPaiementDTO);
        TransactionPaiement transactionPaiement = transactionPaiementMapper.toEntity(transactionPaiementDTO);
        transactionPaiement = transactionPaiementRepository.save(transactionPaiement);
        return transactionPaiementMapper.toDto(transactionPaiement);
    }

    @Override
    public Optional<TransactionPaiementDTO> partialUpdate(TransactionPaiementDTO transactionPaiementDTO) {
        LOG.debug("Request to partially update TransactionPaiement : {}", transactionPaiementDTO);

        return transactionPaiementRepository
            .findById(transactionPaiementDTO.getId())
            .map(existingTransactionPaiement -> {
                transactionPaiementMapper.partialUpdate(existingTransactionPaiement, transactionPaiementDTO);

                return existingTransactionPaiement;
            })
            .map(transactionPaiementRepository::save)
            .map(transactionPaiementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionPaiementDTO> findOne(UUID id) {
        LOG.debug("Request to get TransactionPaiement : {}", id);
        return transactionPaiementRepository.findById(id).map(transactionPaiementMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete TransactionPaiement : {}", id);
        transactionPaiementRepository.deleteById(id);
    }
}
