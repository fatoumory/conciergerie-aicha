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
import sn.orange.conciergerie.repository.EligibiliteServiceRepository;
import sn.orange.conciergerie.service.EligibiliteServiceService;
import sn.orange.conciergerie.service.dto.EligibiliteServiceDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.EligibiliteService}.
 */
@RestController
@RequestMapping("/api/eligibilite-services")
public class EligibiliteServiceResource {

    private static final Logger LOG = LoggerFactory.getLogger(EligibiliteServiceResource.class);

    private static final String ENTITY_NAME = "eligibiliteService";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final EligibiliteServiceService eligibiliteServiceService;

    private final EligibiliteServiceRepository eligibiliteServiceRepository;

    public EligibiliteServiceResource(
        EligibiliteServiceService eligibiliteServiceService,
        EligibiliteServiceRepository eligibiliteServiceRepository
    ) {
        this.eligibiliteServiceService = eligibiliteServiceService;
        this.eligibiliteServiceRepository = eligibiliteServiceRepository;
    }

    /**
     * {@code POST  /eligibilite-services} : Create a new eligibiliteService.
     *
     * @param eligibiliteServiceDTO the eligibiliteServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eligibiliteServiceDTO, or with status {@code 400 (Bad Request)} if the eligibiliteService already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EligibiliteServiceDTO> createEligibiliteService(@Valid @RequestBody EligibiliteServiceDTO eligibiliteServiceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EligibiliteService : {}", eligibiliteServiceDTO);
        if (eligibiliteServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new eligibiliteService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        eligibiliteServiceDTO = eligibiliteServiceService.save(eligibiliteServiceDTO);
        return ResponseEntity.created(new URI("/api/eligibilite-services/" + eligibiliteServiceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, eligibiliteServiceDTO.getId().toString()))
            .body(eligibiliteServiceDTO);
    }

    /**
     * {@code PUT  /eligibilite-services/:id} : Updates an existing eligibiliteService.
     *
     * @param id the id of the eligibiliteServiceDTO to save.
     * @param eligibiliteServiceDTO the eligibiliteServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eligibiliteServiceDTO,
     * or with status {@code 400 (Bad Request)} if the eligibiliteServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eligibiliteServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EligibiliteServiceDTO> updateEligibiliteService(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody EligibiliteServiceDTO eligibiliteServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EligibiliteService : {}, {}", id, eligibiliteServiceDTO);
        if (eligibiliteServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eligibiliteServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eligibiliteServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        eligibiliteServiceDTO = eligibiliteServiceService.update(eligibiliteServiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eligibiliteServiceDTO.getId().toString()))
            .body(eligibiliteServiceDTO);
    }

    /**
     * {@code PATCH  /eligibilite-services/:id} : Partial updates given fields of an existing eligibiliteService, field will ignore if it is null
     *
     * @param id the id of the eligibiliteServiceDTO to save.
     * @param eligibiliteServiceDTO the eligibiliteServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eligibiliteServiceDTO,
     * or with status {@code 400 (Bad Request)} if the eligibiliteServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eligibiliteServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eligibiliteServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EligibiliteServiceDTO> partialUpdateEligibiliteService(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody EligibiliteServiceDTO eligibiliteServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update EligibiliteService : {}, {}", id, eligibiliteServiceDTO);
        if (eligibiliteServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eligibiliteServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eligibiliteServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EligibiliteServiceDTO> result = eligibiliteServiceService.partialUpdate(eligibiliteServiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eligibiliteServiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /eligibilite-services} : get all the Eligibilite Services.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Eligibilite Services in body.
     */
    @GetMapping("")
    public List<EligibiliteServiceDTO> getAllEligibiliteServices(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all EligibiliteServices");
        if (eagerload) {
            return eligibiliteServiceService.findAllWithEagerRelationships();
        } else {
            return eligibiliteServiceService.findAll();
        }
    }

    /**
     * {@code GET  /eligibilite-services/:id} : get the "id" eligibiliteService.
     *
     * @param id the id of the eligibiliteServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eligibiliteServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EligibiliteServiceDTO> getEligibiliteService(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get EligibiliteService : {}", id);
        Optional<EligibiliteServiceDTO> eligibiliteServiceDTO = eligibiliteServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eligibiliteServiceDTO);
    }

    /**
     * {@code DELETE  /eligibilite-services/:id} : delete the "id" eligibiliteService.
     *
     * @param id the id of the eligibiliteServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEligibiliteService(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete EligibiliteService : {}", id);
        eligibiliteServiceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
