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
import sn.orange.conciergerie.repository.HistoriqueStatutDemandeRepository;
import sn.orange.conciergerie.service.HistoriqueStatutDemandeService;
import sn.orange.conciergerie.service.dto.HistoriqueStatutDemandeDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.HistoriqueStatutDemande}.
 */
@RestController
@RequestMapping("/api/historique-statut-demandes")
public class HistoriqueStatutDemandeResource {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueStatutDemandeResource.class);

    private static final String ENTITY_NAME = "historiqueStatutDemande";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final HistoriqueStatutDemandeService historiqueStatutDemandeService;

    private final HistoriqueStatutDemandeRepository historiqueStatutDemandeRepository;

    public HistoriqueStatutDemandeResource(
        HistoriqueStatutDemandeService historiqueStatutDemandeService,
        HistoriqueStatutDemandeRepository historiqueStatutDemandeRepository
    ) {
        this.historiqueStatutDemandeService = historiqueStatutDemandeService;
        this.historiqueStatutDemandeRepository = historiqueStatutDemandeRepository;
    }

    /**
     * {@code POST  /historique-statut-demandes} : Create a new historiqueStatutDemande.
     *
     * @param historiqueStatutDemandeDTO the historiqueStatutDemandeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiqueStatutDemandeDTO, or with status {@code 400 (Bad Request)} if the historiqueStatutDemande already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HistoriqueStatutDemandeDTO> createHistoriqueStatutDemande(
        @Valid @RequestBody HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save HistoriqueStatutDemande : {}", historiqueStatutDemandeDTO);
        if (historiqueStatutDemandeDTO.getId() != null) {
            throw new BadRequestAlertException("A new historiqueStatutDemande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        historiqueStatutDemandeDTO = historiqueStatutDemandeService.save(historiqueStatutDemandeDTO);
        return ResponseEntity.created(new URI("/api/historique-statut-demandes/" + historiqueStatutDemandeDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, historiqueStatutDemandeDTO.getId().toString())
            )
            .body(historiqueStatutDemandeDTO);
    }

    /**
     * {@code PUT  /historique-statut-demandes/:id} : Updates an existing historiqueStatutDemande.
     *
     * @param id the id of the historiqueStatutDemandeDTO to save.
     * @param historiqueStatutDemandeDTO the historiqueStatutDemandeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueStatutDemandeDTO,
     * or with status {@code 400 (Bad Request)} if the historiqueStatutDemandeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiqueStatutDemandeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HistoriqueStatutDemandeDTO> updateHistoriqueStatutDemande(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HistoriqueStatutDemande : {}, {}", id, historiqueStatutDemandeDTO);
        if (historiqueStatutDemandeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiqueStatutDemandeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiqueStatutDemandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        historiqueStatutDemandeDTO = historiqueStatutDemandeService.update(historiqueStatutDemandeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueStatutDemandeDTO.getId().toString()))
            .body(historiqueStatutDemandeDTO);
    }

    /**
     * {@code PATCH  /historique-statut-demandes/:id} : Partial updates given fields of an existing historiqueStatutDemande, field will ignore if it is null
     *
     * @param id the id of the historiqueStatutDemandeDTO to save.
     * @param historiqueStatutDemandeDTO the historiqueStatutDemandeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueStatutDemandeDTO,
     * or with status {@code 400 (Bad Request)} if the historiqueStatutDemandeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historiqueStatutDemandeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historiqueStatutDemandeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HistoriqueStatutDemandeDTO> partialUpdateHistoriqueStatutDemande(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update HistoriqueStatutDemande : {}, {}", id, historiqueStatutDemandeDTO);
        if (historiqueStatutDemandeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiqueStatutDemandeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiqueStatutDemandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoriqueStatutDemandeDTO> result = historiqueStatutDemandeService.partialUpdate(historiqueStatutDemandeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueStatutDemandeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /historique-statut-demandes} : get all the Historique Statut Demandes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Historique Statut Demandes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HistoriqueStatutDemandeDTO>> getAllHistoriqueStatutDemandes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of HistoriqueStatutDemandes");
        Page<HistoriqueStatutDemandeDTO> page;
        if (eagerload) {
            page = historiqueStatutDemandeService.findAllWithEagerRelationships(pageable);
        } else {
            page = historiqueStatutDemandeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historique-statut-demandes/:id} : get the "id" historiqueStatutDemande.
     *
     * @param id the id of the historiqueStatutDemandeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiqueStatutDemandeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueStatutDemandeDTO> getHistoriqueStatutDemande(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get HistoriqueStatutDemande : {}", id);
        Optional<HistoriqueStatutDemandeDTO> historiqueStatutDemandeDTO = historiqueStatutDemandeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiqueStatutDemandeDTO);
    }

    /**
     * {@code DELETE  /historique-statut-demandes/:id} : delete the "id" historiqueStatutDemande.
     *
     * @param id the id of the historiqueStatutDemandeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoriqueStatutDemande(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete HistoriqueStatutDemande : {}", id);
        historiqueStatutDemandeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
