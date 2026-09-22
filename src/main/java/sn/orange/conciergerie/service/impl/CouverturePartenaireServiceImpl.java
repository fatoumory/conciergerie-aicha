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
import sn.orange.conciergerie.domain.CouverturePartenaire;
import sn.orange.conciergerie.repository.CouverturePartenaireRepository;
import sn.orange.conciergerie.service.CouverturePartenaireService;
import sn.orange.conciergerie.service.dto.CouverturePartenaireDTO;
import sn.orange.conciergerie.service.mapper.CouverturePartenaireMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.CouverturePartenaire}.
 */
@Service
@Transactional
public class CouverturePartenaireServiceImpl implements CouverturePartenaireService {

    private static final Logger LOG = LoggerFactory.getLogger(CouverturePartenaireServiceImpl.class);

    private final CouverturePartenaireRepository couverturePartenaireRepository;

    private final CouverturePartenaireMapper couverturePartenaireMapper;

    public CouverturePartenaireServiceImpl(
        CouverturePartenaireRepository couverturePartenaireRepository,
        CouverturePartenaireMapper couverturePartenaireMapper
    ) {
        this.couverturePartenaireRepository = couverturePartenaireRepository;
        this.couverturePartenaireMapper = couverturePartenaireMapper;
    }

    @Override
    public CouverturePartenaireDTO save(CouverturePartenaireDTO couverturePartenaireDTO) {
        LOG.debug("Request to save CouverturePartenaire : {}", couverturePartenaireDTO);
        CouverturePartenaire couverturePartenaire = couverturePartenaireMapper.toEntity(couverturePartenaireDTO);
        couverturePartenaire = couverturePartenaireRepository.save(couverturePartenaire);
        return couverturePartenaireMapper.toDto(couverturePartenaire);
    }

    @Override
    public CouverturePartenaireDTO update(CouverturePartenaireDTO couverturePartenaireDTO) {
        LOG.debug("Request to update CouverturePartenaire : {}", couverturePartenaireDTO);
        CouverturePartenaire couverturePartenaire = couverturePartenaireMapper.toEntity(couverturePartenaireDTO);
        couverturePartenaire = couverturePartenaireRepository.save(couverturePartenaire);
        return couverturePartenaireMapper.toDto(couverturePartenaire);
    }

    @Override
    public Optional<CouverturePartenaireDTO> partialUpdate(CouverturePartenaireDTO couverturePartenaireDTO) {
        LOG.debug("Request to partially update CouverturePartenaire : {}", couverturePartenaireDTO);

        return couverturePartenaireRepository
            .findById(couverturePartenaireDTO.getId())
            .map(existingCouverturePartenaire -> {
                couverturePartenaireMapper.partialUpdate(existingCouverturePartenaire, couverturePartenaireDTO);

                return existingCouverturePartenaire;
            })
            .map(couverturePartenaireRepository::save)
            .map(couverturePartenaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouverturePartenaireDTO> findAll() {
        LOG.debug("Request to get all CouverturePartenaires");
        return couverturePartenaireRepository
            .findAll()
            .stream()
            .map(couverturePartenaireMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<CouverturePartenaireDTO> findAllWithEagerRelationships() {
        return couverturePartenaireRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(couverturePartenaireMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CouverturePartenaireDTO> findOne(UUID id) {
        LOG.debug("Request to get CouverturePartenaire : {}", id);
        return couverturePartenaireRepository.findOneWithEagerRelationships(id).map(couverturePartenaireMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete CouverturePartenaire : {}", id);
        couverturePartenaireRepository.deleteById(id);
    }
}
