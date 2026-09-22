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
import sn.orange.conciergerie.repository.IdempotencyKeyRepository;
import sn.orange.conciergerie.service.IdempotencyKeyService;
import sn.orange.conciergerie.service.dto.IdempotencyKeyDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.IdempotencyKey}.
 */
@RestController
@RequestMapping("/api/idempotency-keys")
public class IdempotencyKeyResource {

    private static final Logger LOG = LoggerFactory.getLogger(IdempotencyKeyResource.class);

    private static final String ENTITY_NAME = "idempotencyKey";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final IdempotencyKeyService idempotencyKeyService;

    private final IdempotencyKeyRepository idempotencyKeyRepository;

    public IdempotencyKeyResource(IdempotencyKeyService idempotencyKeyService, IdempotencyKeyRepository idempotencyKeyRepository) {
        this.idempotencyKeyService = idempotencyKeyService;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
    }

    /**
     * {@code POST  /idempotency-keys} : Create a new idempotencyKey.
     *
     * @param idempotencyKeyDTO the idempotencyKeyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new idempotencyKeyDTO, or with status {@code 400 (Bad Request)} if the idempotencyKey already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IdempotencyKeyDTO> createIdempotencyKey(@Valid @RequestBody IdempotencyKeyDTO idempotencyKeyDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save IdempotencyKey : {}", idempotencyKeyDTO);
        if (idempotencyKeyDTO.getId() != null) {
            throw new BadRequestAlertException("A new idempotencyKey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        idempotencyKeyDTO = idempotencyKeyService.save(idempotencyKeyDTO);
        return ResponseEntity.created(new URI("/api/idempotency-keys/" + idempotencyKeyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, idempotencyKeyDTO.getId().toString()))
            .body(idempotencyKeyDTO);
    }

    /**
     * {@code PUT  /idempotency-keys/:id} : Updates an existing idempotencyKey.
     *
     * @param id the id of the idempotencyKeyDTO to save.
     * @param idempotencyKeyDTO the idempotencyKeyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated idempotencyKeyDTO,
     * or with status {@code 400 (Bad Request)} if the idempotencyKeyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the idempotencyKeyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IdempotencyKeyDTO> updateIdempotencyKey(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody IdempotencyKeyDTO idempotencyKeyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IdempotencyKey : {}, {}", id, idempotencyKeyDTO);
        if (idempotencyKeyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, idempotencyKeyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!idempotencyKeyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        idempotencyKeyDTO = idempotencyKeyService.update(idempotencyKeyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idempotencyKeyDTO.getId().toString()))
            .body(idempotencyKeyDTO);
    }

    /**
     * {@code PATCH  /idempotency-keys/:id} : Partial updates given fields of an existing idempotencyKey, field will ignore if it is null
     *
     * @param id the id of the idempotencyKeyDTO to save.
     * @param idempotencyKeyDTO the idempotencyKeyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated idempotencyKeyDTO,
     * or with status {@code 400 (Bad Request)} if the idempotencyKeyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the idempotencyKeyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the idempotencyKeyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IdempotencyKeyDTO> partialUpdateIdempotencyKey(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody IdempotencyKeyDTO idempotencyKeyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update IdempotencyKey : {}, {}", id, idempotencyKeyDTO);
        if (idempotencyKeyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, idempotencyKeyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!idempotencyKeyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IdempotencyKeyDTO> result = idempotencyKeyService.partialUpdate(idempotencyKeyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idempotencyKeyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /idempotency-keys} : get all the Idempotency Keys.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Idempotency Keys in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IdempotencyKeyDTO>> getAllIdempotencyKeys(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of IdempotencyKeys");
        Page<IdempotencyKeyDTO> page = idempotencyKeyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /idempotency-keys/:id} : get the "id" idempotencyKey.
     *
     * @param id the id of the idempotencyKeyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the idempotencyKeyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IdempotencyKeyDTO> getIdempotencyKey(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get IdempotencyKey : {}", id);
        Optional<IdempotencyKeyDTO> idempotencyKeyDTO = idempotencyKeyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(idempotencyKeyDTO);
    }

    /**
     * {@code DELETE  /idempotency-keys/:id} : delete the "id" idempotencyKey.
     *
     * @param id the id of the idempotencyKeyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdempotencyKey(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete IdempotencyKey : {}", id);
        idempotencyKeyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
