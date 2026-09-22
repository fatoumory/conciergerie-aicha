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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.orange.conciergerie.repository.JournalAuditRepository;
import sn.orange.conciergerie.service.JournalAuditService;
import sn.orange.conciergerie.service.dto.JournalAuditDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.JournalAudit}.
 */
@RestController
@RequestMapping("/api/journal-audits")
public class JournalAuditResource {

    private static final Logger LOG = LoggerFactory.getLogger(JournalAuditResource.class);

    private static final String ENTITY_NAME = "journalAudit";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final JournalAuditService journalAuditService;

    private final JournalAuditRepository journalAuditRepository;

    public JournalAuditResource(JournalAuditService journalAuditService, JournalAuditRepository journalAuditRepository) {
        this.journalAuditService = journalAuditService;
        this.journalAuditRepository = journalAuditRepository;
    }

    /**
     * {@code POST  /journal-audits} : Create a new journalAudit.
     *
     * @param journalAuditDTO the journalAuditDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new journalAuditDTO, or with status {@code 400 (Bad Request)} if the journalAudit already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<JournalAuditDTO> createJournalAudit(@Valid @RequestBody JournalAuditDTO journalAuditDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save JournalAudit : {}", journalAuditDTO);
        if (journalAuditDTO.getId() != null) {
            throw new BadRequestAlertException("A new journalAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        journalAuditDTO = journalAuditService.save(journalAuditDTO);
        return ResponseEntity.created(new URI("/api/journal-audits/" + journalAuditDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, journalAuditDTO.getId().toString()))
            .body(journalAuditDTO);
    }

    /**
     * {@code PUT  /journal-audits/:id} : Updates an existing journalAudit.
     *
     * @param id the id of the journalAuditDTO to save.
     * @param journalAuditDTO the journalAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated journalAuditDTO,
     * or with status {@code 400 (Bad Request)} if the journalAuditDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the journalAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<JournalAuditDTO> updateJournalAudit(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody JournalAuditDTO journalAuditDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update JournalAudit : {}, {}", id, journalAuditDTO);
        if (journalAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, journalAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!journalAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        journalAuditDTO = journalAuditService.update(journalAuditDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, journalAuditDTO.getId().toString()))
            .body(journalAuditDTO);
    }

    /**
     * {@code PATCH  /journal-audits/:id} : Partial updates given fields of an existing journalAudit, field will ignore if it is null
     *
     * @param id the id of the journalAuditDTO to save.
     * @param journalAuditDTO the journalAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated journalAuditDTO,
     * or with status {@code 400 (Bad Request)} if the journalAuditDTO is not valid,
     * or with status {@code 404 (Not Found)} if the journalAuditDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the journalAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JournalAuditDTO> partialUpdateJournalAudit(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody JournalAuditDTO journalAuditDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update JournalAudit : {}, {}", id, journalAuditDTO);
        if (journalAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, journalAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!journalAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JournalAuditDTO> result = journalAuditService.partialUpdate(journalAuditDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, journalAuditDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /journal-audits} : get all the Journal Audits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Journal Audits in body.
     */
    @GetMapping("")
    public ResponseEntity<List<JournalAuditDTO>> getAllJournalAudits(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of JournalAudits");
        Page<JournalAuditDTO> page = journalAuditService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /journal-audits/:id} : get the "id" journalAudit.
     *
     * @param id the id of the journalAuditDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the journalAuditDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JournalAuditDTO> getJournalAudit(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get JournalAudit : {}", id);
        Optional<JournalAuditDTO> journalAuditDTO = journalAuditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(journalAuditDTO);
    }

    /**
     * {@code DELETE  /journal-audits/:id} : delete the "id" journalAudit.
     *
     * @param id the id of the journalAuditDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJournalAudit(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete JournalAudit : {}", id);
        journalAuditService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
