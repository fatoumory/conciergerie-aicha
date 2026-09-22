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
import sn.orange.conciergerie.repository.PartenaireZoneRepository;
import sn.orange.conciergerie.service.PartenaireZoneService;
import sn.orange.conciergerie.service.dto.PartenaireZoneDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.PartenaireZone}.
 */
@RestController
@RequestMapping("/api/partenaire-zones")
public class PartenaireZoneResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartenaireZoneResource.class);

    private static final String ENTITY_NAME = "partenaireZone";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final PartenaireZoneService partenaireZoneService;

    private final PartenaireZoneRepository partenaireZoneRepository;

    public PartenaireZoneResource(PartenaireZoneService partenaireZoneService, PartenaireZoneRepository partenaireZoneRepository) {
        this.partenaireZoneService = partenaireZoneService;
        this.partenaireZoneRepository = partenaireZoneRepository;
    }

    /**
     * {@code POST  /partenaire-zones} : Create a new partenaireZone.
     *
     * @param partenaireZoneDTO the partenaireZoneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partenaireZoneDTO, or with status {@code 400 (Bad Request)} if the partenaireZone already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartenaireZoneDTO> createPartenaireZone(@Valid @RequestBody PartenaireZoneDTO partenaireZoneDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PartenaireZone : {}", partenaireZoneDTO);
        if (partenaireZoneDTO.getId() != null) {
            throw new BadRequestAlertException("A new partenaireZone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partenaireZoneDTO = partenaireZoneService.save(partenaireZoneDTO);
        return ResponseEntity.created(new URI("/api/partenaire-zones/" + partenaireZoneDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, partenaireZoneDTO.getId().toString()))
            .body(partenaireZoneDTO);
    }

    /**
     * {@code PUT  /partenaire-zones/:id} : Updates an existing partenaireZone.
     *
     * @param id the id of the partenaireZoneDTO to save.
     * @param partenaireZoneDTO the partenaireZoneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partenaireZoneDTO,
     * or with status {@code 400 (Bad Request)} if the partenaireZoneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partenaireZoneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartenaireZoneDTO> updatePartenaireZone(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody PartenaireZoneDTO partenaireZoneDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PartenaireZone : {}, {}", id, partenaireZoneDTO);
        if (partenaireZoneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partenaireZoneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partenaireZoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partenaireZoneDTO = partenaireZoneService.update(partenaireZoneDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partenaireZoneDTO.getId().toString()))
            .body(partenaireZoneDTO);
    }

    /**
     * {@code PATCH  /partenaire-zones/:id} : Partial updates given fields of an existing partenaireZone, field will ignore if it is null
     *
     * @param id the id of the partenaireZoneDTO to save.
     * @param partenaireZoneDTO the partenaireZoneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partenaireZoneDTO,
     * or with status {@code 400 (Bad Request)} if the partenaireZoneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the partenaireZoneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the partenaireZoneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartenaireZoneDTO> partialUpdatePartenaireZone(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody PartenaireZoneDTO partenaireZoneDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PartenaireZone : {}, {}", id, partenaireZoneDTO);
        if (partenaireZoneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partenaireZoneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partenaireZoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartenaireZoneDTO> result = partenaireZoneService.partialUpdate(partenaireZoneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partenaireZoneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /partenaire-zones} : get all the Partenaire Zones.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Partenaire Zones in body.
     */
    @GetMapping("")
    public List<PartenaireZoneDTO> getAllPartenaireZones(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all PartenaireZones");
        if (eagerload) {
            return partenaireZoneService.findAllWithEagerRelationships();
        } else {
            return partenaireZoneService.findAll();
        }
    }

    /**
     * {@code GET  /partenaire-zones/:id} : get the "id" partenaireZone.
     *
     * @param id the id of the partenaireZoneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partenaireZoneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartenaireZoneDTO> getPartenaireZone(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get PartenaireZone : {}", id);
        Optional<PartenaireZoneDTO> partenaireZoneDTO = partenaireZoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partenaireZoneDTO);
    }

    /**
     * {@code DELETE  /partenaire-zones/:id} : delete the "id" partenaireZone.
     *
     * @param id the id of the partenaireZoneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartenaireZone(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete PartenaireZone : {}", id);
        partenaireZoneService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
