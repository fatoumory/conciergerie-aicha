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
import sn.orange.conciergerie.domain.Profil;
import sn.orange.conciergerie.repository.ProfilRepository;
import sn.orange.conciergerie.service.ProfilService;
import sn.orange.conciergerie.service.dto.ProfilDTO;
import sn.orange.conciergerie.service.mapper.ProfilMapper;

/**
 * Service Implementation for managing {@link sn.orange.conciergerie.domain.Profil}.
 */
@Service
@Transactional
public class ProfilServiceImpl implements ProfilService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfilServiceImpl.class);

    private final ProfilRepository profilRepository;

    private final ProfilMapper profilMapper;

    public ProfilServiceImpl(ProfilRepository profilRepository, ProfilMapper profilMapper) {
        this.profilRepository = profilRepository;
        this.profilMapper = profilMapper;
    }

    @Override
    public ProfilDTO save(ProfilDTO profilDTO) {
        LOG.debug("Request to save Profil : {}", profilDTO);
        Profil profil = profilMapper.toEntity(profilDTO);
        profil = profilRepository.save(profil);
        return profilMapper.toDto(profil);
    }

    @Override
    public ProfilDTO update(ProfilDTO profilDTO) {
        LOG.debug("Request to update Profil : {}", profilDTO);
        Profil profil = profilMapper.toEntity(profilDTO);
        profil = profilRepository.save(profil);
        return profilMapper.toDto(profil);
    }

    @Override
    public Optional<ProfilDTO> partialUpdate(ProfilDTO profilDTO) {
        LOG.debug("Request to partially update Profil : {}", profilDTO);

        return profilRepository
            .findById(profilDTO.getId())
            .map(existingProfil -> {
                profilMapper.partialUpdate(existingProfil, profilDTO);

                return existingProfil;
            })
            .map(profilRepository::save)
            .map(profilMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfilDTO> findAll() {
        LOG.debug("Request to get all Profils");
        return profilRepository.findAll().stream().map(profilMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<ProfilDTO> findAllWithEagerRelationships() {
        return profilRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(profilMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfilDTO> findOne(UUID id) {
        LOG.debug("Request to get Profil : {}", id);
        return profilRepository.findOneWithEagerRelationships(id).map(profilMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Profil : {}", id);
        profilRepository.deleteById(id);
    }
}
