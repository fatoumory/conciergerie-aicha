package sn.orange.conciergerie.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.orange.conciergerie.repository.PartenaireRepository;
import sn.orange.conciergerie.service.PartenaireService;
import sn.orange.conciergerie.service.dto.PartenaireDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.Partenaire}.
 */
@RestController
@RequestMapping("/api/partenaires")
public class PartenaireResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartenaireResource.class);

    private static final String ENTITY_NAME = "partenaire";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final PartenaireService partenaireService;

    private final PartenaireRepository partenaireRepository;

    public PartenaireResource(PartenaireService partenaireService, PartenaireRepository partenaireRepository) {
        this.partenaireService = partenaireService;
        this.partenaireRepository = partenaireRepository;
    }

    /**
     * {@code POST  /partenaires} : Create a new partenaire.
     *
     * @param partenaireDTO the partenaireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partenaireDTO, or with status {@code 400 (Bad Request)} if the partenaire already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartenaireDTO> createPartenaire(@Valid @RequestBody PartenaireDTO partenaireDTO) throws URISyntaxException {
        LOG.debug("REST request to save Partenaire : {}", partenaireDTO);
        if (partenaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new partenaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partenaireDTO = partenaireService.save(partenaireDTO);
        return ResponseEntity.created(new URI("/api/partenaires/" + partenaireDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, partenaireDTO.getId().toString()))
            .body(partenaireDTO);
    }

    /**
     * {@code PUT  /partenaires/:id} : Updates an existing partenaire.
     *
     * @param id the id of the partenaireDTO to save.
     * @param partenaireDTO the partenaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partenaireDTO,
     * or with status {@code 400 (Bad Request)} if the partenaireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partenaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartenaireDTO> updatePartenaire(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody PartenaireDTO partenaireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Partenaire : {}, {}", id, partenaireDTO);
        if (partenaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partenaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partenaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partenaireDTO = partenaireService.update(partenaireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partenaireDTO.getId().toString()))
            .body(partenaireDTO);
    }

    /**
     * {@code PATCH  /partenaires/:id} : Partial updates given fields of an existing partenaire, field will ignore if it is null
     *
     * @param id the id of the partenaireDTO to save.
     * @param partenaireDTO the partenaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partenaireDTO,
     * or with status {@code 400 (Bad Request)} if the partenaireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the partenaireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the partenaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartenaireDTO> partialUpdatePartenaire(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody PartenaireDTO partenaireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Partenaire : {}, {}", id, partenaireDTO);
        if (partenaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partenaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partenaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartenaireDTO> result = partenaireService.partialUpdate(partenaireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partenaireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /partenaires} : get all the Partenaires.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Partenaires in body.
     */
    @GetMapping("")
    public List<PartenaireDTO> getAllPartenaires() {
        LOG.debug("REST request to get all Partenaires");
        return partenaireService.findAll();
    }

    /**
     * {@code GET  /partenaires/:id} : get the "id" partenaire.
     *
     * @param id the id of the partenaireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partenaireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartenaireDTO> getPartenaire(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Partenaire : {}", id);
        Optional<PartenaireDTO> partenaireDTO = partenaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partenaireDTO);
    }

    /**
     * {@code DELETE  /partenaires/:id} : delete the "id" partenaire.
     *
     * @param id the id of the partenaireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartenaire(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Partenaire : {}", id);
        partenaireService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
