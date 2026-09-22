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
import sn.orange.conciergerie.repository.ConsommationQuotaRepository;
import sn.orange.conciergerie.service.ConsommationQuotaQueryService;
import sn.orange.conciergerie.service.ConsommationQuotaService;
import sn.orange.conciergerie.service.criteria.ConsommationQuotaCriteria;
import sn.orange.conciergerie.service.dto.ConsommationQuotaDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.ConsommationQuota}.
 */
@RestController
@RequestMapping("/api/consommation-quotas")
public class ConsommationQuotaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConsommationQuotaResource.class);

    private static final String ENTITY_NAME = "consommationQuota";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final ConsommationQuotaService consommationQuotaService;

    private final ConsommationQuotaRepository consommationQuotaRepository;

    private final ConsommationQuotaQueryService consommationQuotaQueryService;

    public ConsommationQuotaResource(
        ConsommationQuotaService consommationQuotaService,
        ConsommationQuotaRepository consommationQuotaRepository,
        ConsommationQuotaQueryService consommationQuotaQueryService
    ) {
        this.consommationQuotaService = consommationQuotaService;
        this.consommationQuotaRepository = consommationQuotaRepository;
        this.consommationQuotaQueryService = consommationQuotaQueryService;
    }

    /**
     * {@code POST  /consommation-quotas} : Create a new consommationQuota.
     *
     * @param consommationQuotaDTO the consommationQuotaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consommationQuotaDTO, or with status {@code 400 (Bad Request)} if the consommationQuota already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConsommationQuotaDTO> createConsommationQuota(@Valid @RequestBody ConsommationQuotaDTO consommationQuotaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ConsommationQuota : {}", consommationQuotaDTO);
        if (consommationQuotaDTO.getId() != null) {
            throw new BadRequestAlertException("A new consommationQuota cannot already have an ID", ENTITY_NAME, "idexists");
        }
        consommationQuotaDTO = consommationQuotaService.save(consommationQuotaDTO);
        return ResponseEntity.created(new URI("/api/consommation-quotas/" + consommationQuotaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, consommationQuotaDTO.getId().toString()))
            .body(consommationQuotaDTO);
    }

    /**
     * {@code PUT  /consommation-quotas/:id} : Updates an existing consommationQuota.
     *
     * @param id the id of the consommationQuotaDTO to save.
     * @param consommationQuotaDTO the consommationQuotaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consommationQuotaDTO,
     * or with status {@code 400 (Bad Request)} if the consommationQuotaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consommationQuotaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConsommationQuotaDTO> updateConsommationQuota(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ConsommationQuotaDTO consommationQuotaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ConsommationQuota : {}, {}", id, consommationQuotaDTO);
        if (consommationQuotaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consommationQuotaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consommationQuotaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        consommationQuotaDTO = consommationQuotaService.update(consommationQuotaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consommationQuotaDTO.getId().toString()))
            .body(consommationQuotaDTO);
    }

    /**
     * {@code PATCH  /consommation-quotas/:id} : Partial updates given fields of an existing consommationQuota, field will ignore if it is null
     *
     * @param id the id of the consommationQuotaDTO to save.
     * @param consommationQuotaDTO the consommationQuotaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consommationQuotaDTO,
     * or with status {@code 400 (Bad Request)} if the consommationQuotaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the consommationQuotaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the consommationQuotaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConsommationQuotaDTO> partialUpdateConsommationQuota(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ConsommationQuotaDTO consommationQuotaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ConsommationQuota : {}, {}", id, consommationQuotaDTO);
        if (consommationQuotaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consommationQuotaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consommationQuotaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConsommationQuotaDTO> result = consommationQuotaService.partialUpdate(consommationQuotaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consommationQuotaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /consommation-quotas} : get all the Consommation Quotas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Consommation Quotas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ConsommationQuotaDTO>> getAllConsommationQuotas(
        ConsommationQuotaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ConsommationQuotas by criteria: {}", criteria);

        Page<ConsommationQuotaDTO> page = consommationQuotaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /consommation-quotas/count} : count all the consommationQuotas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countConsommationQuotas(ConsommationQuotaCriteria criteria) {
        LOG.debug("REST request to count ConsommationQuotas by criteria: {}", criteria);
        return ResponseEntity.ok().body(consommationQuotaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /consommation-quotas/:id} : get the "id" consommationQuota.
     *
     * @param id the id of the consommationQuotaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consommationQuotaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConsommationQuotaDTO> getConsommationQuota(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get ConsommationQuota : {}", id);
        Optional<ConsommationQuotaDTO> consommationQuotaDTO = consommationQuotaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consommationQuotaDTO);
    }

    /**
     * {@code DELETE  /consommation-quotas/:id} : delete the "id" consommationQuota.
     *
     * @param id the id of the consommationQuotaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsommationQuota(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete ConsommationQuota : {}", id);
        consommationQuotaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
