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
import sn.orange.conciergerie.repository.CompteStockRepository;
import sn.orange.conciergerie.service.CompteStockService;
import sn.orange.conciergerie.service.dto.CompteStockDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.CompteStock}.
 */
@RestController
@RequestMapping("/api/compte-stocks")
public class CompteStockResource {

    private static final Logger LOG = LoggerFactory.getLogger(CompteStockResource.class);

    private static final String ENTITY_NAME = "compteStock";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final CompteStockService compteStockService;

    private final CompteStockRepository compteStockRepository;

    public CompteStockResource(CompteStockService compteStockService, CompteStockRepository compteStockRepository) {
        this.compteStockService = compteStockService;
        this.compteStockRepository = compteStockRepository;
    }

    /**
     * {@code POST  /compte-stocks} : Create a new compteStock.
     *
     * @param compteStockDTO the compteStockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compteStockDTO, or with status {@code 400 (Bad Request)} if the compteStock already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CompteStockDTO> createCompteStock(@Valid @RequestBody CompteStockDTO compteStockDTO) throws URISyntaxException {
        LOG.debug("REST request to save CompteStock : {}", compteStockDTO);
        if (compteStockDTO.getId() != null) {
            throw new BadRequestAlertException("A new compteStock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        compteStockDTO = compteStockService.save(compteStockDTO);
        return ResponseEntity.created(new URI("/api/compte-stocks/" + compteStockDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, compteStockDTO.getId().toString()))
            .body(compteStockDTO);
    }

    /**
     * {@code PUT  /compte-stocks/:id} : Updates an existing compteStock.
     *
     * @param id the id of the compteStockDTO to save.
     * @param compteStockDTO the compteStockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compteStockDTO,
     * or with status {@code 400 (Bad Request)} if the compteStockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compteStockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompteStockDTO> updateCompteStock(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CompteStockDTO compteStockDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CompteStock : {}, {}", id, compteStockDTO);
        if (compteStockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteStockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteStockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        compteStockDTO = compteStockService.update(compteStockDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteStockDTO.getId().toString()))
            .body(compteStockDTO);
    }

    /**
     * {@code PATCH  /compte-stocks/:id} : Partial updates given fields of an existing compteStock, field will ignore if it is null
     *
     * @param id the id of the compteStockDTO to save.
     * @param compteStockDTO the compteStockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compteStockDTO,
     * or with status {@code 400 (Bad Request)} if the compteStockDTO is not valid,
     * or with status {@code 404 (Not Found)} if the compteStockDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the compteStockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompteStockDTO> partialUpdateCompteStock(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CompteStockDTO compteStockDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CompteStock : {}, {}", id, compteStockDTO);
        if (compteStockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteStockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteStockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompteStockDTO> result = compteStockService.partialUpdate(compteStockDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteStockDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /compte-stocks} : get all the Compte Stocks.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Compte Stocks in body.
     */
    @GetMapping("")
    public List<CompteStockDTO> getAllCompteStocks(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all CompteStocks");
        if (eagerload) {
            return compteStockService.findAllWithEagerRelationships();
        } else {
            return compteStockService.findAll();
        }
    }

    /**
     * {@code GET  /compte-stocks/:id} : get the "id" compteStock.
     *
     * @param id the id of the compteStockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compteStockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompteStockDTO> getCompteStock(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get CompteStock : {}", id);
        Optional<CompteStockDTO> compteStockDTO = compteStockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compteStockDTO);
    }

    /**
     * {@code DELETE  /compte-stocks/:id} : delete the "id" compteStock.
     *
     * @param id the id of the compteStockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompteStock(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete CompteStock : {}", id);
        compteStockService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
