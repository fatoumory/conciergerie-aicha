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
import sn.orange.conciergerie.repository.TypeDemandeRepository;
import sn.orange.conciergerie.service.TypeDemandeService;
import sn.orange.conciergerie.service.dto.TypeDemandeDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.TypeDemande}.
 */
@RestController
@RequestMapping("/api/type-demandes")
public class TypeDemandeResource {

    private static final Logger LOG = LoggerFactory.getLogger(TypeDemandeResource.class);

    private static final String ENTITY_NAME = "typeDemande";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final TypeDemandeService typeDemandeService;

    private final TypeDemandeRepository typeDemandeRepository;

    public TypeDemandeResource(TypeDemandeService typeDemandeService, TypeDemandeRepository typeDemandeRepository) {
        this.typeDemandeService = typeDemandeService;
        this.typeDemandeRepository = typeDemandeRepository;
    }

    /**
     * {@code POST  /type-demandes} : Create a new typeDemande.
     *
     * @param typeDemandeDTO the typeDemandeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeDemandeDTO, or with status {@code 400 (Bad Request)} if the typeDemande already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeDemandeDTO> createTypeDemande(@Valid @RequestBody TypeDemandeDTO typeDemandeDTO) throws URISyntaxException {
        LOG.debug("REST request to save TypeDemande : {}", typeDemandeDTO);
        if (typeDemandeDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeDemande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        typeDemandeDTO = typeDemandeService.save(typeDemandeDTO);
        return ResponseEntity.created(new URI("/api/type-demandes/" + typeDemandeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, typeDemandeDTO.getId().toString()))
            .body(typeDemandeDTO);
    }

    /**
     * {@code PUT  /type-demandes/:id} : Updates an existing typeDemande.
     *
     * @param id the id of the typeDemandeDTO to save.
     * @param typeDemandeDTO the typeDemandeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeDemandeDTO,
     * or with status {@code 400 (Bad Request)} if the typeDemandeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeDemandeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeDemandeDTO> updateTypeDemande(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody TypeDemandeDTO typeDemandeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TypeDemande : {}, {}", id, typeDemandeDTO);
        if (typeDemandeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeDemandeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeDemandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        typeDemandeDTO = typeDemandeService.update(typeDemandeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeDemandeDTO.getId().toString()))
            .body(typeDemandeDTO);
    }

    /**
     * {@code PATCH  /type-demandes/:id} : Partial updates given fields of an existing typeDemande, field will ignore if it is null
     *
     * @param id the id of the typeDemandeDTO to save.
     * @param typeDemandeDTO the typeDemandeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeDemandeDTO,
     * or with status {@code 400 (Bad Request)} if the typeDemandeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeDemandeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeDemandeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeDemandeDTO> partialUpdateTypeDemande(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody TypeDemandeDTO typeDemandeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update TypeDemande : {}, {}", id, typeDemandeDTO);
        if (typeDemandeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeDemandeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeDemandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeDemandeDTO> result = typeDemandeService.partialUpdate(typeDemandeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeDemandeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-demandes} : get all the Type Demandes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Type Demandes in body.
     */
    @GetMapping("")
    public List<TypeDemandeDTO> getAllTypeDemandes() {
        LOG.debug("REST request to get all TypeDemandes");
        return typeDemandeService.findAll();
    }

    /**
     * {@code GET  /type-demandes/:id} : get the "id" typeDemande.
     *
     * @param id the id of the typeDemandeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeDemandeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeDemandeDTO> getTypeDemande(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get TypeDemande : {}", id);
        Optional<TypeDemandeDTO> typeDemandeDTO = typeDemandeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeDemandeDTO);
    }

    /**
     * {@code DELETE  /type-demandes/:id} : delete the "id" typeDemande.
     *
     * @param id the id of the typeDemandeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeDemande(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete TypeDemande : {}", id);
        typeDemandeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
