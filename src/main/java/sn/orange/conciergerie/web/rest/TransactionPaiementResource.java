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
import sn.orange.conciergerie.repository.TransactionPaiementRepository;
import sn.orange.conciergerie.service.TransactionPaiementQueryService;
import sn.orange.conciergerie.service.TransactionPaiementService;
import sn.orange.conciergerie.service.criteria.TransactionPaiementCriteria;
import sn.orange.conciergerie.service.dto.TransactionPaiementDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.TransactionPaiement}.
 */
@RestController
@RequestMapping("/api/transaction-paiements")
public class TransactionPaiementResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionPaiementResource.class);

    private static final String ENTITY_NAME = "transactionPaiement";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final TransactionPaiementService transactionPaiementService;

    private final TransactionPaiementRepository transactionPaiementRepository;

    private final TransactionPaiementQueryService transactionPaiementQueryService;

    public TransactionPaiementResource(
        TransactionPaiementService transactionPaiementService,
        TransactionPaiementRepository transactionPaiementRepository,
        TransactionPaiementQueryService transactionPaiementQueryService
    ) {
        this.transactionPaiementService = transactionPaiementService;
        this.transactionPaiementRepository = transactionPaiementRepository;
        this.transactionPaiementQueryService = transactionPaiementQueryService;
    }

    /**
     * {@code POST  /transaction-paiements} : Create a new transactionPaiement.
     *
     * @param transactionPaiementDTO the transactionPaiementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionPaiementDTO, or with status {@code 400 (Bad Request)} if the transactionPaiement already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransactionPaiementDTO> createTransactionPaiement(
        @Valid @RequestBody TransactionPaiementDTO transactionPaiementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save TransactionPaiement : {}", transactionPaiementDTO);
        if (transactionPaiementDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionPaiement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transactionPaiementDTO = transactionPaiementService.save(transactionPaiementDTO);
        return ResponseEntity.created(new URI("/api/transaction-paiements/" + transactionPaiementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transactionPaiementDTO.getId().toString()))
            .body(transactionPaiementDTO);
    }

    /**
     * {@code PUT  /transaction-paiements/:id} : Updates an existing transactionPaiement.
     *
     * @param id the id of the transactionPaiementDTO to save.
     * @param transactionPaiementDTO the transactionPaiementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionPaiementDTO,
     * or with status {@code 400 (Bad Request)} if the transactionPaiementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionPaiementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionPaiementDTO> updateTransactionPaiement(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody TransactionPaiementDTO transactionPaiementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TransactionPaiement : {}, {}", id, transactionPaiementDTO);
        if (transactionPaiementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionPaiementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionPaiementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transactionPaiementDTO = transactionPaiementService.update(transactionPaiementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionPaiementDTO.getId().toString()))
            .body(transactionPaiementDTO);
    }

    /**
     * {@code PATCH  /transaction-paiements/:id} : Partial updates given fields of an existing transactionPaiement, field will ignore if it is null
     *
     * @param id the id of the transactionPaiementDTO to save.
     * @param transactionPaiementDTO the transactionPaiementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionPaiementDTO,
     * or with status {@code 400 (Bad Request)} if the transactionPaiementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionPaiementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionPaiementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionPaiementDTO> partialUpdateTransactionPaiement(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody TransactionPaiementDTO transactionPaiementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update TransactionPaiement : {}, {}", id, transactionPaiementDTO);
        if (transactionPaiementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionPaiementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionPaiementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionPaiementDTO> result = transactionPaiementService.partialUpdate(transactionPaiementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionPaiementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-paiements} : get all the Transaction Paiements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Transaction Paiements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransactionPaiementDTO>> getAllTransactionPaiements(
        TransactionPaiementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TransactionPaiements by criteria: {}", criteria);

        Page<TransactionPaiementDTO> page = transactionPaiementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-paiements/count} : count all the transactionPaiements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransactionPaiements(TransactionPaiementCriteria criteria) {
        LOG.debug("REST request to count TransactionPaiements by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionPaiementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-paiements/:id} : get the "id" transactionPaiement.
     *
     * @param id the id of the transactionPaiementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionPaiementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionPaiementDTO> getTransactionPaiement(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get TransactionPaiement : {}", id);
        Optional<TransactionPaiementDTO> transactionPaiementDTO = transactionPaiementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionPaiementDTO);
    }

    /**
     * {@code DELETE  /transaction-paiements/:id} : delete the "id" transactionPaiement.
     *
     * @param id the id of the transactionPaiementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionPaiement(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete TransactionPaiement : {}", id);
        transactionPaiementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
