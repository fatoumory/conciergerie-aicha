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
import sn.orange.conciergerie.repository.CouverturePartenaireRepository;
import sn.orange.conciergerie.service.CouverturePartenaireService;
import sn.orange.conciergerie.service.dto.CouverturePartenaireDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.CouverturePartenaire}.
 */
@RestController
@RequestMapping("/api/couverture-partenaires")
public class CouverturePartenaireResource {

    private static final Logger LOG = LoggerFactory.getLogger(CouverturePartenaireResource.class);

    private static final String ENTITY_NAME = "couverturePartenaire";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final CouverturePartenaireService couverturePartenaireService;

    private final CouverturePartenaireRepository couverturePartenaireRepository;

    public CouverturePartenaireResource(
        CouverturePartenaireService couverturePartenaireService,
        CouverturePartenaireRepository couverturePartenaireRepository
    ) {
        this.couverturePartenaireService = couverturePartenaireService;
        this.couverturePartenaireRepository = couverturePartenaireRepository;
    }

    /**
     * {@code POST  /couverture-partenaires} : Create a new couverturePartenaire.
     *
     * @param couverturePartenaireDTO the couverturePartenaireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new couverturePartenaireDTO, or with status {@code 400 (Bad Request)} if the couverturePartenaire already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CouverturePartenaireDTO> createCouverturePartenaire(
        @Valid @RequestBody CouverturePartenaireDTO couverturePartenaireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save CouverturePartenaire : {}", couverturePartenaireDTO);
        if (couverturePartenaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new couverturePartenaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        couverturePartenaireDTO = couverturePartenaireService.save(couverturePartenaireDTO);
        return ResponseEntity.created(new URI("/api/couverture-partenaires/" + couverturePartenaireDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, couverturePartenaireDTO.getId().toString()))
            .body(couverturePartenaireDTO);
    }

    /**
     * {@code PUT  /couverture-partenaires/:id} : Updates an existing couverturePartenaire.
     *
     * @param id the id of the couverturePartenaireDTO to save.
     * @param couverturePartenaireDTO the couverturePartenaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated couverturePartenaireDTO,
     * or with status {@code 400 (Bad Request)} if the couverturePartenaireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the couverturePartenaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CouverturePartenaireDTO> updateCouverturePartenaire(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CouverturePartenaireDTO couverturePartenaireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CouverturePartenaire : {}, {}", id, couverturePartenaireDTO);
        if (couverturePartenaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, couverturePartenaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!couverturePartenaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        couverturePartenaireDTO = couverturePartenaireService.update(couverturePartenaireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, couverturePartenaireDTO.getId().toString()))
            .body(couverturePartenaireDTO);
    }

    /**
     * {@code PATCH  /couverture-partenaires/:id} : Partial updates given fields of an existing couverturePartenaire, field will ignore if it is null
     *
     * @param id the id of the couverturePartenaireDTO to save.
     * @param couverturePartenaireDTO the couverturePartenaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated couverturePartenaireDTO,
     * or with status {@code 400 (Bad Request)} if the couverturePartenaireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the couverturePartenaireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the couverturePartenaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CouverturePartenaireDTO> partialUpdateCouverturePartenaire(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CouverturePartenaireDTO couverturePartenaireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CouverturePartenaire : {}, {}", id, couverturePartenaireDTO);
        if (couverturePartenaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, couverturePartenaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!couverturePartenaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CouverturePartenaireDTO> result = couverturePartenaireService.partialUpdate(couverturePartenaireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, couverturePartenaireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /couverture-partenaires} : get all the Couverture Partenaires.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Couverture Partenaires in body.
     */
    @GetMapping("")
    public List<CouverturePartenaireDTO> getAllCouverturePartenaires(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all CouverturePartenaires");
        if (eagerload) {
            return couverturePartenaireService.findAllWithEagerRelationships();
        } else {
            return couverturePartenaireService.findAll();
        }
    }

    /**
     * {@code GET  /couverture-partenaires/:id} : get the "id" couverturePartenaire.
     *
     * @param id the id of the couverturePartenaireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the couverturePartenaireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CouverturePartenaireDTO> getCouverturePartenaire(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get CouverturePartenaire : {}", id);
        Optional<CouverturePartenaireDTO> couverturePartenaireDTO = couverturePartenaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(couverturePartenaireDTO);
    }

    /**
     * {@code DELETE  /couverture-partenaires/:id} : delete the "id" couverturePartenaire.
     *
     * @param id the id of the couverturePartenaireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCouverturePartenaire(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete CouverturePartenaire : {}", id);
        couverturePartenaireService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
