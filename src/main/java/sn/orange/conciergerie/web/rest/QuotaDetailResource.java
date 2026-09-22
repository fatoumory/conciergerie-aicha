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
import sn.orange.conciergerie.repository.QuotaDetailRepository;
import sn.orange.conciergerie.service.QuotaDetailService;
import sn.orange.conciergerie.service.dto.QuotaDetailDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.QuotaDetail}.
 */
@RestController
@RequestMapping("/api/quota-details")
public class QuotaDetailResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuotaDetailResource.class);

    private static final String ENTITY_NAME = "quotaDetail";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final QuotaDetailService quotaDetailService;

    private final QuotaDetailRepository quotaDetailRepository;

    public QuotaDetailResource(QuotaDetailService quotaDetailService, QuotaDetailRepository quotaDetailRepository) {
        this.quotaDetailService = quotaDetailService;
        this.quotaDetailRepository = quotaDetailRepository;
    }

    /**
     * {@code POST  /quota-details} : Create a new quotaDetail.
     *
     * @param quotaDetailDTO the quotaDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quotaDetailDTO, or with status {@code 400 (Bad Request)} if the quotaDetail already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuotaDetailDTO> createQuotaDetail(@Valid @RequestBody QuotaDetailDTO quotaDetailDTO) throws URISyntaxException {
        LOG.debug("REST request to save QuotaDetail : {}", quotaDetailDTO);
        if (quotaDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new quotaDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quotaDetailDTO = quotaDetailService.save(quotaDetailDTO);
        return ResponseEntity.created(new URI("/api/quota-details/" + quotaDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quotaDetailDTO.getId().toString()))
            .body(quotaDetailDTO);
    }

    /**
     * {@code PUT  /quota-details/:id} : Updates an existing quotaDetail.
     *
     * @param id the id of the quotaDetailDTO to save.
     * @param quotaDetailDTO the quotaDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotaDetailDTO,
     * or with status {@code 400 (Bad Request)} if the quotaDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quotaDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuotaDetailDTO> updateQuotaDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody QuotaDetailDTO quotaDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuotaDetail : {}, {}", id, quotaDetailDTO);
        if (quotaDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotaDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quotaDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quotaDetailDTO = quotaDetailService.update(quotaDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quotaDetailDTO.getId().toString()))
            .body(quotaDetailDTO);
    }

    /**
     * {@code PATCH  /quota-details/:id} : Partial updates given fields of an existing quotaDetail, field will ignore if it is null
     *
     * @param id the id of the quotaDetailDTO to save.
     * @param quotaDetailDTO the quotaDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotaDetailDTO,
     * or with status {@code 400 (Bad Request)} if the quotaDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quotaDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quotaDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuotaDetailDTO> partialUpdateQuotaDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody QuotaDetailDTO quotaDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update QuotaDetail : {}, {}", id, quotaDetailDTO);
        if (quotaDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotaDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quotaDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuotaDetailDTO> result = quotaDetailService.partialUpdate(quotaDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quotaDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quota-details} : get all the Quota Details.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Quota Details in body.
     */
    @GetMapping("")
    public List<QuotaDetailDTO> getAllQuotaDetails(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all QuotaDetails");
        if (eagerload) {
            return quotaDetailService.findAllWithEagerRelationships();
        } else {
            return quotaDetailService.findAll();
        }
    }

    /**
     * {@code GET  /quota-details/:id} : get the "id" quotaDetail.
     *
     * @param id the id of the quotaDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quotaDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuotaDetailDTO> getQuotaDetail(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get QuotaDetail : {}", id);
        Optional<QuotaDetailDTO> quotaDetailDTO = quotaDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quotaDetailDTO);
    }

    /**
     * {@code DELETE  /quota-details/:id} : delete the "id" quotaDetail.
     *
     * @param id the id of the quotaDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuotaDetail(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete QuotaDetail : {}", id);
        quotaDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
