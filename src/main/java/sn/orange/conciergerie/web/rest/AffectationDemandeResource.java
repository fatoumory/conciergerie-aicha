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
import sn.orange.conciergerie.repository.AffectationDemandeRepository;
import sn.orange.conciergerie.service.AffectationDemandeService;
import sn.orange.conciergerie.service.dto.AffectationDemandeDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.AffectationDemande}.
 */
@RestController
@RequestMapping("/api/affectation-demandes")
public class AffectationDemandeResource {

    private static final Logger LOG = LoggerFactory.getLogger(AffectationDemandeResource.class);

    private static final String ENTITY_NAME = "affectationDemande";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final AffectationDemandeService affectationDemandeService;

    private final AffectationDemandeRepository affectationDemandeRepository;

    public AffectationDemandeResource(
        AffectationDemandeService affectationDemandeService,
        AffectationDemandeRepository affectationDemandeRepository
    ) {
        this.affectationDemandeService = affectationDemandeService;
        this.affectationDemandeRepository = affectationDemandeRepository;
    }

    /**
     * {@code POST  /affectation-demandes} : Create a new affectationDemande.
     *
     * @param affectationDemandeDTO the affectationDemandeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new affectationDemandeDTO, or with status {@code 400 (Bad Request)} if the affectationDemande already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AffectationDemandeDTO> createAffectationDemande(@Valid @RequestBody AffectationDemandeDTO affectationDemandeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AffectationDemande : {}", affectationDemandeDTO);
        if (affectationDemandeDTO.getId() != null) {
            throw new BadRequestAlertException("A new affectationDemande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        affectationDemandeDTO = affectationDemandeService.save(affectationDemandeDTO);
        return ResponseEntity.created(new URI("/api/affectation-demandes/" + affectationDemandeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, affectationDemandeDTO.getId().toString()))
            .body(affectationDemandeDTO);
    }

    /**
     * {@code PUT  /affectation-demandes/:id} : Updates an existing affectationDemande.
     *
     * @param id the id of the affectationDemandeDTO to save.
     * @param affectationDemandeDTO the affectationDemandeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectationDemandeDTO,
     * or with status {@code 400 (Bad Request)} if the affectationDemandeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the affectationDemandeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AffectationDemandeDTO> updateAffectationDemande(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AffectationDemandeDTO affectationDemandeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AffectationDemande : {}, {}", id, affectationDemandeDTO);
        if (affectationDemandeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectationDemandeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affectationDemandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        affectationDemandeDTO = affectationDemandeService.update(affectationDemandeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectationDemandeDTO.getId().toString()))
            .body(affectationDemandeDTO);
    }

    /**
     * {@code PATCH  /affectation-demandes/:id} : Partial updates given fields of an existing affectationDemande, field will ignore if it is null
     *
     * @param id the id of the affectationDemandeDTO to save.
     * @param affectationDemandeDTO the affectationDemandeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectationDemandeDTO,
     * or with status {@code 400 (Bad Request)} if the affectationDemandeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the affectationDemandeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the affectationDemandeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AffectationDemandeDTO> partialUpdateAffectationDemande(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AffectationDemandeDTO affectationDemandeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update AffectationDemande : {}, {}", id, affectationDemandeDTO);
        if (affectationDemandeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectationDemandeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affectationDemandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AffectationDemandeDTO> result = affectationDemandeService.partialUpdate(affectationDemandeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectationDemandeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /affectation-demandes} : get all the Affectation Demandes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Affectation Demandes in body.
     */
    @GetMapping("")
    public List<AffectationDemandeDTO> getAllAffectationDemandes(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all AffectationDemandes");
        if (eagerload) {
            return affectationDemandeService.findAllWithEagerRelationships();
        } else {
            return affectationDemandeService.findAll();
        }
    }

    /**
     * {@code GET  /affectation-demandes/:id} : get the "id" affectationDemande.
     *
     * @param id the id of the affectationDemandeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the affectationDemandeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AffectationDemandeDTO> getAffectationDemande(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AffectationDemande : {}", id);
        Optional<AffectationDemandeDTO> affectationDemandeDTO = affectationDemandeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(affectationDemandeDTO);
    }

    /**
     * {@code DELETE  /affectation-demandes/:id} : delete the "id" affectationDemande.
     *
     * @param id the id of the affectationDemandeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAffectationDemande(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AffectationDemande : {}", id);
        affectationDemandeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
