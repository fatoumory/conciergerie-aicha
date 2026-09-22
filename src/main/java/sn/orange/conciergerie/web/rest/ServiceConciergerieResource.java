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
import sn.orange.conciergerie.repository.ServiceConciergerieRepository;
import sn.orange.conciergerie.service.ServiceConciergerieService;
import sn.orange.conciergerie.service.dto.ServiceConciergerieDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.ServiceConciergerie}.
 */
@RestController
@RequestMapping("/api/service-conciergeries")
public class ServiceConciergerieResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceConciergerieResource.class);

    private static final String ENTITY_NAME = "serviceConciergerie";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final ServiceConciergerieService serviceConciergerieService;

    private final ServiceConciergerieRepository serviceConciergerieRepository;

    public ServiceConciergerieResource(
        ServiceConciergerieService serviceConciergerieService,
        ServiceConciergerieRepository serviceConciergerieRepository
    ) {
        this.serviceConciergerieService = serviceConciergerieService;
        this.serviceConciergerieRepository = serviceConciergerieRepository;
    }

    /**
     * {@code POST  /service-conciergeries} : Create a new serviceConciergerie.
     *
     * @param serviceConciergerieDTO the serviceConciergerieDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceConciergerieDTO, or with status {@code 400 (Bad Request)} if the serviceConciergerie already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceConciergerieDTO> createServiceConciergerie(
        @Valid @RequestBody ServiceConciergerieDTO serviceConciergerieDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ServiceConciergerie : {}", serviceConciergerieDTO);
        if (serviceConciergerieDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceConciergerie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceConciergerieDTO = serviceConciergerieService.save(serviceConciergerieDTO);
        return ResponseEntity.created(new URI("/api/service-conciergeries/" + serviceConciergerieDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, serviceConciergerieDTO.getId().toString()))
            .body(serviceConciergerieDTO);
    }

    /**
     * {@code PUT  /service-conciergeries/:id} : Updates an existing serviceConciergerie.
     *
     * @param id the id of the serviceConciergerieDTO to save.
     * @param serviceConciergerieDTO the serviceConciergerieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceConciergerieDTO,
     * or with status {@code 400 (Bad Request)} if the serviceConciergerieDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceConciergerieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceConciergerieDTO> updateServiceConciergerie(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ServiceConciergerieDTO serviceConciergerieDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServiceConciergerie : {}, {}", id, serviceConciergerieDTO);
        if (serviceConciergerieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceConciergerieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceConciergerieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceConciergerieDTO = serviceConciergerieService.update(serviceConciergerieDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceConciergerieDTO.getId().toString()))
            .body(serviceConciergerieDTO);
    }

    /**
     * {@code PATCH  /service-conciergeries/:id} : Partial updates given fields of an existing serviceConciergerie, field will ignore if it is null
     *
     * @param id the id of the serviceConciergerieDTO to save.
     * @param serviceConciergerieDTO the serviceConciergerieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceConciergerieDTO,
     * or with status {@code 400 (Bad Request)} if the serviceConciergerieDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceConciergerieDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceConciergerieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceConciergerieDTO> partialUpdateServiceConciergerie(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ServiceConciergerieDTO serviceConciergerieDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ServiceConciergerie : {}, {}", id, serviceConciergerieDTO);
        if (serviceConciergerieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceConciergerieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceConciergerieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceConciergerieDTO> result = serviceConciergerieService.partialUpdate(serviceConciergerieDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceConciergerieDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-conciergeries} : get all the Service Conciergeries.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Service Conciergeries in body.
     */
    @GetMapping("")
    public List<ServiceConciergerieDTO> getAllServiceConciergeries(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("comptestock-is-null".equals(filter)) {
            LOG.debug("REST request to get all ServiceConciergeries where compteStock is null");
            return serviceConciergerieService.findAllWhereCompteStockIsNull();
        }
        LOG.debug("REST request to get all ServiceConciergeries");
        if (eagerload) {
            return serviceConciergerieService.findAllWithEagerRelationships();
        } else {
            return serviceConciergerieService.findAll();
        }
    }

    /**
     * {@code GET  /service-conciergeries/:id} : get the "id" serviceConciergerie.
     *
     * @param id the id of the serviceConciergerieDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceConciergerieDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceConciergerieDTO> getServiceConciergerie(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get ServiceConciergerie : {}", id);
        Optional<ServiceConciergerieDTO> serviceConciergerieDTO = serviceConciergerieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceConciergerieDTO);
    }

    /**
     * {@code DELETE  /service-conciergeries/:id} : delete the "id" serviceConciergerie.
     *
     * @param id the id of the serviceConciergerieDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceConciergerie(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete ServiceConciergerie : {}", id);
        serviceConciergerieService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
