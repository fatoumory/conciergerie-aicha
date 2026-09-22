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
import sn.orange.conciergerie.repository.UtilisationCodeQrRepository;
import sn.orange.conciergerie.service.UtilisationCodeQrService;
import sn.orange.conciergerie.service.dto.UtilisationCodeQrDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.UtilisationCodeQr}.
 */
@RestController
@RequestMapping("/api/utilisation-code-qrs")
public class UtilisationCodeQrResource {

    private static final Logger LOG = LoggerFactory.getLogger(UtilisationCodeQrResource.class);

    private static final String ENTITY_NAME = "utilisationCodeQr";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final UtilisationCodeQrService utilisationCodeQrService;

    private final UtilisationCodeQrRepository utilisationCodeQrRepository;

    public UtilisationCodeQrResource(
        UtilisationCodeQrService utilisationCodeQrService,
        UtilisationCodeQrRepository utilisationCodeQrRepository
    ) {
        this.utilisationCodeQrService = utilisationCodeQrService;
        this.utilisationCodeQrRepository = utilisationCodeQrRepository;
    }

    /**
     * {@code POST  /utilisation-code-qrs} : Create a new utilisationCodeQr.
     *
     * @param utilisationCodeQrDTO the utilisationCodeQrDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new utilisationCodeQrDTO, or with status {@code 400 (Bad Request)} if the utilisationCodeQr already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UtilisationCodeQrDTO> createUtilisationCodeQr(@Valid @RequestBody UtilisationCodeQrDTO utilisationCodeQrDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UtilisationCodeQr : {}", utilisationCodeQrDTO);
        if (utilisationCodeQrDTO.getId() != null) {
            throw new BadRequestAlertException("A new utilisationCodeQr cannot already have an ID", ENTITY_NAME, "idexists");
        }
        utilisationCodeQrDTO = utilisationCodeQrService.save(utilisationCodeQrDTO);
        return ResponseEntity.created(new URI("/api/utilisation-code-qrs/" + utilisationCodeQrDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, utilisationCodeQrDTO.getId().toString()))
            .body(utilisationCodeQrDTO);
    }

    /**
     * {@code PUT  /utilisation-code-qrs/:id} : Updates an existing utilisationCodeQr.
     *
     * @param id the id of the utilisationCodeQrDTO to save.
     * @param utilisationCodeQrDTO the utilisationCodeQrDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilisationCodeQrDTO,
     * or with status {@code 400 (Bad Request)} if the utilisationCodeQrDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the utilisationCodeQrDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UtilisationCodeQrDTO> updateUtilisationCodeQr(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody UtilisationCodeQrDTO utilisationCodeQrDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UtilisationCodeQr : {}, {}", id, utilisationCodeQrDTO);
        if (utilisationCodeQrDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisationCodeQrDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisationCodeQrRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        utilisationCodeQrDTO = utilisationCodeQrService.update(utilisationCodeQrDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilisationCodeQrDTO.getId().toString()))
            .body(utilisationCodeQrDTO);
    }

    /**
     * {@code PATCH  /utilisation-code-qrs/:id} : Partial updates given fields of an existing utilisationCodeQr, field will ignore if it is null
     *
     * @param id the id of the utilisationCodeQrDTO to save.
     * @param utilisationCodeQrDTO the utilisationCodeQrDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilisationCodeQrDTO,
     * or with status {@code 400 (Bad Request)} if the utilisationCodeQrDTO is not valid,
     * or with status {@code 404 (Not Found)} if the utilisationCodeQrDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the utilisationCodeQrDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UtilisationCodeQrDTO> partialUpdateUtilisationCodeQr(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody UtilisationCodeQrDTO utilisationCodeQrDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update UtilisationCodeQr : {}, {}", id, utilisationCodeQrDTO);
        if (utilisationCodeQrDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisationCodeQrDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisationCodeQrRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UtilisationCodeQrDTO> result = utilisationCodeQrService.partialUpdate(utilisationCodeQrDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilisationCodeQrDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /utilisation-code-qrs} : get all the Utilisation Code Qrs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Utilisation Code Qrs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UtilisationCodeQrDTO>> getAllUtilisationCodeQrs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of UtilisationCodeQrs");
        Page<UtilisationCodeQrDTO> page;
        if (eagerload) {
            page = utilisationCodeQrService.findAllWithEagerRelationships(pageable);
        } else {
            page = utilisationCodeQrService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /utilisation-code-qrs/:id} : get the "id" utilisationCodeQr.
     *
     * @param id the id of the utilisationCodeQrDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the utilisationCodeQrDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UtilisationCodeQrDTO> getUtilisationCodeQr(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get UtilisationCodeQr : {}", id);
        Optional<UtilisationCodeQrDTO> utilisationCodeQrDTO = utilisationCodeQrService.findOne(id);
        return ResponseUtil.wrapOrNotFound(utilisationCodeQrDTO);
    }

    /**
     * {@code DELETE  /utilisation-code-qrs/:id} : delete the "id" utilisationCodeQr.
     *
     * @param id the id of the utilisationCodeQrDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisationCodeQr(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete UtilisationCodeQr : {}", id);
        utilisationCodeQrService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
