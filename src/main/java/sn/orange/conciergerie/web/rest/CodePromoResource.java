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
import sn.orange.conciergerie.repository.CodePromoRepository;
import sn.orange.conciergerie.service.CodePromoService;
import sn.orange.conciergerie.service.dto.CodePromoDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.CodePromo}.
 */
@RestController
@RequestMapping("/api/code-promos")
public class CodePromoResource {

    private static final Logger LOG = LoggerFactory.getLogger(CodePromoResource.class);

    private static final String ENTITY_NAME = "codePromo";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final CodePromoService codePromoService;

    private final CodePromoRepository codePromoRepository;

    public CodePromoResource(CodePromoService codePromoService, CodePromoRepository codePromoRepository) {
        this.codePromoService = codePromoService;
        this.codePromoRepository = codePromoRepository;
    }

    /**
     * {@code POST  /code-promos} : Create a new codePromo.
     *
     * @param codePromoDTO the codePromoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new codePromoDTO, or with status {@code 400 (Bad Request)} if the codePromo already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CodePromoDTO> createCodePromo(@Valid @RequestBody CodePromoDTO codePromoDTO) throws URISyntaxException {
        LOG.debug("REST request to save CodePromo : {}", codePromoDTO);
        if (codePromoDTO.getId() != null) {
            throw new BadRequestAlertException("A new codePromo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        codePromoDTO = codePromoService.save(codePromoDTO);
        return ResponseEntity.created(new URI("/api/code-promos/" + codePromoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, codePromoDTO.getId().toString()))
            .body(codePromoDTO);
    }

    /**
     * {@code PUT  /code-promos/:id} : Updates an existing codePromo.
     *
     * @param id the id of the codePromoDTO to save.
     * @param codePromoDTO the codePromoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codePromoDTO,
     * or with status {@code 400 (Bad Request)} if the codePromoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the codePromoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CodePromoDTO> updateCodePromo(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CodePromoDTO codePromoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CodePromo : {}, {}", id, codePromoDTO);
        if (codePromoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codePromoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codePromoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        codePromoDTO = codePromoService.update(codePromoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codePromoDTO.getId().toString()))
            .body(codePromoDTO);
    }

    /**
     * {@code PATCH  /code-promos/:id} : Partial updates given fields of an existing codePromo, field will ignore if it is null
     *
     * @param id the id of the codePromoDTO to save.
     * @param codePromoDTO the codePromoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codePromoDTO,
     * or with status {@code 400 (Bad Request)} if the codePromoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the codePromoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the codePromoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CodePromoDTO> partialUpdateCodePromo(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CodePromoDTO codePromoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CodePromo : {}, {}", id, codePromoDTO);
        if (codePromoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codePromoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codePromoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CodePromoDTO> result = codePromoService.partialUpdate(codePromoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codePromoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /code-promos} : get all the Code Promos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Code Promos in body.
     */
    @GetMapping("")
    public List<CodePromoDTO> getAllCodePromos() {
        LOG.debug("REST request to get all CodePromos");
        return codePromoService.findAll();
    }

    /**
     * {@code GET  /code-promos/:id} : get the "id" codePromo.
     *
     * @param id the id of the codePromoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the codePromoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CodePromoDTO> getCodePromo(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get CodePromo : {}", id);
        Optional<CodePromoDTO> codePromoDTO = codePromoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(codePromoDTO);
    }

    /**
     * {@code DELETE  /code-promos/:id} : delete the "id" codePromo.
     *
     * @param id the id of the codePromoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCodePromo(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete CodePromo : {}", id);
        codePromoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
