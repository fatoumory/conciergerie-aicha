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
import sn.orange.conciergerie.repository.CodeQrServiceRepository;
import sn.orange.conciergerie.service.CodeQrServiceService;
import sn.orange.conciergerie.service.dto.CodeQrServiceDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.CodeQrService}.
 */
@RestController
@RequestMapping("/api/code-qr-services")
public class CodeQrServiceResource {

    private static final Logger LOG = LoggerFactory.getLogger(CodeQrServiceResource.class);

    private static final String ENTITY_NAME = "codeQrService";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final CodeQrServiceService codeQrServiceService;

    private final CodeQrServiceRepository codeQrServiceRepository;

    public CodeQrServiceResource(CodeQrServiceService codeQrServiceService, CodeQrServiceRepository codeQrServiceRepository) {
        this.codeQrServiceService = codeQrServiceService;
        this.codeQrServiceRepository = codeQrServiceRepository;
    }

    /**
     * {@code POST  /code-qr-services} : Create a new codeQrService.
     *
     * @param codeQrServiceDTO the codeQrServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new codeQrServiceDTO, or with status {@code 400 (Bad Request)} if the codeQrService already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CodeQrServiceDTO> createCodeQrService(@Valid @RequestBody CodeQrServiceDTO codeQrServiceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CodeQrService : {}", codeQrServiceDTO);
        if (codeQrServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new codeQrService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        codeQrServiceDTO = codeQrServiceService.save(codeQrServiceDTO);
        return ResponseEntity.created(new URI("/api/code-qr-services/" + codeQrServiceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, codeQrServiceDTO.getId().toString()))
            .body(codeQrServiceDTO);
    }

    /**
     * {@code PUT  /code-qr-services/:id} : Updates an existing codeQrService.
     *
     * @param id the id of the codeQrServiceDTO to save.
     * @param codeQrServiceDTO the codeQrServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeQrServiceDTO,
     * or with status {@code 400 (Bad Request)} if the codeQrServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the codeQrServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CodeQrServiceDTO> updateCodeQrService(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CodeQrServiceDTO codeQrServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CodeQrService : {}, {}", id, codeQrServiceDTO);
        if (codeQrServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeQrServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeQrServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        codeQrServiceDTO = codeQrServiceService.update(codeQrServiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codeQrServiceDTO.getId().toString()))
            .body(codeQrServiceDTO);
    }

    /**
     * {@code PATCH  /code-qr-services/:id} : Partial updates given fields of an existing codeQrService, field will ignore if it is null
     *
     * @param id the id of the codeQrServiceDTO to save.
     * @param codeQrServiceDTO the codeQrServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeQrServiceDTO,
     * or with status {@code 400 (Bad Request)} if the codeQrServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the codeQrServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the codeQrServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CodeQrServiceDTO> partialUpdateCodeQrService(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CodeQrServiceDTO codeQrServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CodeQrService : {}, {}", id, codeQrServiceDTO);
        if (codeQrServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeQrServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeQrServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CodeQrServiceDTO> result = codeQrServiceService.partialUpdate(codeQrServiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codeQrServiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /code-qr-services} : get all the Code Qr Services.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Code Qr Services in body.
     */
    @GetMapping("")
    public List<CodeQrServiceDTO> getAllCodeQrServices() {
        LOG.debug("REST request to get all CodeQrServices");
        return codeQrServiceService.findAll();
    }

    /**
     * {@code GET  /code-qr-services/:id} : get the "id" codeQrService.
     *
     * @param id the id of the codeQrServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the codeQrServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CodeQrServiceDTO> getCodeQrService(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get CodeQrService : {}", id);
        Optional<CodeQrServiceDTO> codeQrServiceDTO = codeQrServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(codeQrServiceDTO);
    }

    /**
     * {@code DELETE  /code-qr-services/:id} : delete the "id" codeQrService.
     *
     * @param id the id of the codeQrServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCodeQrService(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete CodeQrService : {}", id);
        codeQrServiceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
