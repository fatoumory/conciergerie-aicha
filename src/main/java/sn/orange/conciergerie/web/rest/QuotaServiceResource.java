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
import sn.orange.conciergerie.repository.QuotaServiceRepository;
import sn.orange.conciergerie.service.QuotaServiceService;
import sn.orange.conciergerie.service.dto.QuotaServiceDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.QuotaService}.
 */
@RestController
@RequestMapping("/api/quota-services")
public class QuotaServiceResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuotaServiceResource.class);

    private static final String ENTITY_NAME = "quotaService";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final QuotaServiceService quotaServiceService;

    private final QuotaServiceRepository quotaServiceRepository;

    public QuotaServiceResource(QuotaServiceService quotaServiceService, QuotaServiceRepository quotaServiceRepository) {
        this.quotaServiceService = quotaServiceService;
        this.quotaServiceRepository = quotaServiceRepository;
    }

    /**
     * {@code POST  /quota-services} : Create a new quotaService.
     *
     * @param quotaServiceDTO the quotaServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quotaServiceDTO, or with status {@code 400 (Bad Request)} if the quotaService already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuotaServiceDTO> createQuotaService(@Valid @RequestBody QuotaServiceDTO quotaServiceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save QuotaService : {}", quotaServiceDTO);
        if (quotaServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new quotaService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quotaServiceDTO = quotaServiceService.save(quotaServiceDTO);
        return ResponseEntity.created(new URI("/api/quota-services/" + quotaServiceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quotaServiceDTO.getId().toString()))
            .body(quotaServiceDTO);
    }

    /**
     * {@code PUT  /quota-services/:id} : Updates an existing quotaService.
     *
     * @param id the id of the quotaServiceDTO to save.
     * @param quotaServiceDTO the quotaServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotaServiceDTO,
     * or with status {@code 400 (Bad Request)} if the quotaServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quotaServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuotaServiceDTO> updateQuotaService(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody QuotaServiceDTO quotaServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuotaService : {}, {}", id, quotaServiceDTO);
        if (quotaServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotaServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quotaServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quotaServiceDTO = quotaServiceService.update(quotaServiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quotaServiceDTO.getId().toString()))
            .body(quotaServiceDTO);
    }

    /**
     * {@code PATCH  /quota-services/:id} : Partial updates given fields of an existing quotaService, field will ignore if it is null
     *
     * @param id the id of the quotaServiceDTO to save.
     * @param quotaServiceDTO the quotaServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotaServiceDTO,
     * or with status {@code 400 (Bad Request)} if the quotaServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quotaServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quotaServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuotaServiceDTO> partialUpdateQuotaService(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody QuotaServiceDTO quotaServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update QuotaService : {}, {}", id, quotaServiceDTO);
        if (quotaServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotaServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quotaServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuotaServiceDTO> result = quotaServiceService.partialUpdate(quotaServiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quotaServiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quota-services} : get all the Quota Services.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Quota Services in body.
     */
    @GetMapping("")
    public List<QuotaServiceDTO> getAllQuotaServices() {
        LOG.debug("REST request to get all QuotaServices");
        return quotaServiceService.findAll();
    }

    /**
     * {@code GET  /quota-services/:id} : get the "id" quotaService.
     *
     * @param id the id of the quotaServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quotaServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuotaServiceDTO> getQuotaService(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get QuotaService : {}", id);
        Optional<QuotaServiceDTO> quotaServiceDTO = quotaServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quotaServiceDTO);
    }

    /**
     * {@code DELETE  /quota-services/:id} : delete the "id" quotaService.
     *
     * @param id the id of the quotaServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuotaService(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete QuotaService : {}", id);
        quotaServiceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
