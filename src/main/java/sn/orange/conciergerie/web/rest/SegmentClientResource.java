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
import sn.orange.conciergerie.repository.SegmentClientRepository;
import sn.orange.conciergerie.service.SegmentClientService;
import sn.orange.conciergerie.service.dto.SegmentClientDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.SegmentClient}.
 */
@RestController
@RequestMapping("/api/segment-clients")
public class SegmentClientResource {

    private static final Logger LOG = LoggerFactory.getLogger(SegmentClientResource.class);

    private static final String ENTITY_NAME = "segmentClient";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final SegmentClientService segmentClientService;

    private final SegmentClientRepository segmentClientRepository;

    public SegmentClientResource(SegmentClientService segmentClientService, SegmentClientRepository segmentClientRepository) {
        this.segmentClientService = segmentClientService;
        this.segmentClientRepository = segmentClientRepository;
    }

    /**
     * {@code POST  /segment-clients} : Create a new segmentClient.
     *
     * @param segmentClientDTO the segmentClientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new segmentClientDTO, or with status {@code 400 (Bad Request)} if the segmentClient already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SegmentClientDTO> createSegmentClient(@Valid @RequestBody SegmentClientDTO segmentClientDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SegmentClient : {}", segmentClientDTO);
        if (segmentClientDTO.getId() != null) {
            throw new BadRequestAlertException("A new segmentClient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        segmentClientDTO = segmentClientService.save(segmentClientDTO);
        return ResponseEntity.created(new URI("/api/segment-clients/" + segmentClientDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, segmentClientDTO.getId().toString()))
            .body(segmentClientDTO);
    }

    /**
     * {@code PUT  /segment-clients/:id} : Updates an existing segmentClient.
     *
     * @param id the id of the segmentClientDTO to save.
     * @param segmentClientDTO the segmentClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated segmentClientDTO,
     * or with status {@code 400 (Bad Request)} if the segmentClientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the segmentClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SegmentClientDTO> updateSegmentClient(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody SegmentClientDTO segmentClientDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SegmentClient : {}, {}", id, segmentClientDTO);
        if (segmentClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, segmentClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!segmentClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        segmentClientDTO = segmentClientService.update(segmentClientDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, segmentClientDTO.getId().toString()))
            .body(segmentClientDTO);
    }

    /**
     * {@code PATCH  /segment-clients/:id} : Partial updates given fields of an existing segmentClient, field will ignore if it is null
     *
     * @param id the id of the segmentClientDTO to save.
     * @param segmentClientDTO the segmentClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated segmentClientDTO,
     * or with status {@code 400 (Bad Request)} if the segmentClientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the segmentClientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the segmentClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SegmentClientDTO> partialUpdateSegmentClient(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SegmentClientDTO segmentClientDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update SegmentClient : {}, {}", id, segmentClientDTO);
        if (segmentClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, segmentClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!segmentClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SegmentClientDTO> result = segmentClientService.partialUpdate(segmentClientDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, segmentClientDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /segment-clients} : get all the Segment Clients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Segment Clients in body.
     */
    @GetMapping("")
    public List<SegmentClientDTO> getAllSegmentClients() {
        LOG.debug("REST request to get all SegmentClients");
        return segmentClientService.findAll();
    }

    /**
     * {@code GET  /segment-clients/:id} : get the "id" segmentClient.
     *
     * @param id the id of the segmentClientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the segmentClientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SegmentClientDTO> getSegmentClient(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get SegmentClient : {}", id);
        Optional<SegmentClientDTO> segmentClientDTO = segmentClientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(segmentClientDTO);
    }

    /**
     * {@code DELETE  /segment-clients/:id} : delete the "id" segmentClient.
     *
     * @param id the id of the segmentClientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSegmentClient(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete SegmentClient : {}", id);
        segmentClientService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
