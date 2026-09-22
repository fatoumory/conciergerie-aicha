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
import sn.orange.conciergerie.repository.StatutDemandeRepository;
import sn.orange.conciergerie.service.StatutDemandeService;
import sn.orange.conciergerie.service.dto.StatutDemandeDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.StatutDemande}.
 */
@RestController
@RequestMapping("/api/statut-demandes")
public class StatutDemandeResource {

    private static final Logger LOG = LoggerFactory.getLogger(StatutDemandeResource.class);

    private static final String ENTITY_NAME = "statutDemande";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final StatutDemandeService statutDemandeService;

    private final StatutDemandeRepository statutDemandeRepository;

    public StatutDemandeResource(StatutDemandeService statutDemandeService, StatutDemandeRepository statutDemandeRepository) {
        this.statutDemandeService = statutDemandeService;
        this.statutDemandeRepository = statutDemandeRepository;
    }

    /**
     * {@code POST  /statut-demandes} : Create a new statutDemande.
     *
     * @param statutDemandeDTO the statutDemandeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new statutDemandeDTO, or with status {@code 400 (Bad Request)} if the statutDemande already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StatutDemandeDTO> createStatutDemande(@Valid @RequestBody StatutDemandeDTO statutDemandeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save StatutDemande : {}", statutDemandeDTO);
        if (statutDemandeDTO.getId() != null) {
            throw new BadRequestAlertException("A new statutDemande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        statutDemandeDTO = statutDemandeService.save(statutDemandeDTO);
        return ResponseEntity.created(new URI("/api/statut-demandes/" + statutDemandeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, statutDemandeDTO.getId().toString()))
            .body(statutDemandeDTO);
    }

    /**
     * {@code PUT  /statut-demandes/:id} : Updates an existing statutDemande.
     *
     * @param id the id of the statutDemandeDTO to save.
     * @param statutDemandeDTO the statutDemandeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statutDemandeDTO,
     * or with status {@code 400 (Bad Request)} if the statutDemandeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statutDemandeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StatutDemandeDTO> updateStatutDemande(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody StatutDemandeDTO statutDemandeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StatutDemande : {}, {}", id, statutDemandeDTO);
        if (statutDemandeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statutDemandeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statutDemandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        statutDemandeDTO = statutDemandeService.update(statutDemandeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statutDemandeDTO.getId().toString()))
            .body(statutDemandeDTO);
    }

    /**
     * {@code PATCH  /statut-demandes/:id} : Partial updates given fields of an existing statutDemande, field will ignore if it is null
     *
     * @param id the id of the statutDemandeDTO to save.
     * @param statutDemandeDTO the statutDemandeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statutDemandeDTO,
     * or with status {@code 400 (Bad Request)} if the statutDemandeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the statutDemandeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the statutDemandeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StatutDemandeDTO> partialUpdateStatutDemande(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody StatutDemandeDTO statutDemandeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update StatutDemande : {}, {}", id, statutDemandeDTO);
        if (statutDemandeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statutDemandeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statutDemandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StatutDemandeDTO> result = statutDemandeService.partialUpdate(statutDemandeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statutDemandeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /statut-demandes} : get all the Statut Demandes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Statut Demandes in body.
     */
    @GetMapping("")
    public List<StatutDemandeDTO> getAllStatutDemandes() {
        LOG.debug("REST request to get all StatutDemandes");
        return statutDemandeService.findAll();
    }

    /**
     * {@code GET  /statut-demandes/:id} : get the "id" statutDemande.
     *
     * @param id the id of the statutDemandeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statutDemandeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StatutDemandeDTO> getStatutDemande(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get StatutDemande : {}", id);
        Optional<StatutDemandeDTO> statutDemandeDTO = statutDemandeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(statutDemandeDTO);
    }

    /**
     * {@code DELETE  /statut-demandes/:id} : delete the "id" statutDemande.
     *
     * @param id the id of the statutDemandeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatutDemande(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete StatutDemande : {}", id);
        statutDemandeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
