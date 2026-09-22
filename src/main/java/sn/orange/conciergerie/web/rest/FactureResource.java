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
import sn.orange.conciergerie.repository.FactureRepository;
import sn.orange.conciergerie.service.FactureService;
import sn.orange.conciergerie.service.dto.FactureDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.Facture}.
 */
@RestController
@RequestMapping("/api/factures")
public class FactureResource {

    private static final Logger LOG = LoggerFactory.getLogger(FactureResource.class);

    private static final String ENTITY_NAME = "facture";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final FactureService factureService;

    private final FactureRepository factureRepository;

    public FactureResource(FactureService factureService, FactureRepository factureRepository) {
        this.factureService = factureService;
        this.factureRepository = factureRepository;
    }

    /**
     * {@code POST  /factures} : Create a new facture.
     *
     * @param factureDTO the factureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factureDTO, or with status {@code 400 (Bad Request)} if the facture already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FactureDTO> createFacture(@Valid @RequestBody FactureDTO factureDTO) throws URISyntaxException {
        LOG.debug("REST request to save Facture : {}", factureDTO);
        if (factureDTO.getId() != null) {
            throw new BadRequestAlertException("A new facture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        factureDTO = factureService.save(factureDTO);
        return ResponseEntity.created(new URI("/api/factures/" + factureDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, factureDTO.getId().toString()))
            .body(factureDTO);
    }

    /**
     * {@code PUT  /factures/:id} : Updates an existing facture.
     *
     * @param id the id of the factureDTO to save.
     * @param factureDTO the factureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factureDTO,
     * or with status {@code 400 (Bad Request)} if the factureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FactureDTO> updateFacture(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody FactureDTO factureDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Facture : {}, {}", id, factureDTO);
        if (factureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        factureDTO = factureService.update(factureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factureDTO.getId().toString()))
            .body(factureDTO);
    }

    /**
     * {@code PATCH  /factures/:id} : Partial updates given fields of an existing facture, field will ignore if it is null
     *
     * @param id the id of the factureDTO to save.
     * @param factureDTO the factureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factureDTO,
     * or with status {@code 400 (Bad Request)} if the factureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the factureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the factureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FactureDTO> partialUpdateFacture(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody FactureDTO factureDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Facture : {}, {}", id, factureDTO);
        if (factureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactureDTO> result = factureService.partialUpdate(factureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /factures} : get all the Factures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Factures in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FactureDTO>> getAllFactures(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Factures");
        Page<FactureDTO> page = factureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /factures/:id} : get the "id" facture.
     *
     * @param id the id of the factureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FactureDTO> getFacture(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Facture : {}", id);
        Optional<FactureDTO> factureDTO = factureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factureDTO);
    }

    /**
     * {@code DELETE  /factures/:id} : delete the "id" facture.
     *
     * @param id the id of the factureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Facture : {}", id);
        factureService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
