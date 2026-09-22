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
import sn.orange.conciergerie.repository.TypeServiceRepository;
import sn.orange.conciergerie.service.TypeServiceService;
import sn.orange.conciergerie.service.dto.TypeServiceDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.TypeService}.
 */
@RestController
@RequestMapping("/api/type-services")
public class TypeServiceResource {

    private static final Logger LOG = LoggerFactory.getLogger(TypeServiceResource.class);

    private static final String ENTITY_NAME = "typeService";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final TypeServiceService typeServiceService;

    private final TypeServiceRepository typeServiceRepository;

    public TypeServiceResource(TypeServiceService typeServiceService, TypeServiceRepository typeServiceRepository) {
        this.typeServiceService = typeServiceService;
        this.typeServiceRepository = typeServiceRepository;
    }

    /**
     * {@code POST  /type-services} : Create a new typeService.
     *
     * @param typeServiceDTO the typeServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeServiceDTO, or with status {@code 400 (Bad Request)} if the typeService already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeServiceDTO> createTypeService(@Valid @RequestBody TypeServiceDTO typeServiceDTO) throws URISyntaxException {
        LOG.debug("REST request to save TypeService : {}", typeServiceDTO);
        if (typeServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        typeServiceDTO = typeServiceService.save(typeServiceDTO);
        return ResponseEntity.created(new URI("/api/type-services/" + typeServiceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, typeServiceDTO.getId().toString()))
            .body(typeServiceDTO);
    }

    /**
     * {@code PUT  /type-services/:id} : Updates an existing typeService.
     *
     * @param id the id of the typeServiceDTO to save.
     * @param typeServiceDTO the typeServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeServiceDTO,
     * or with status {@code 400 (Bad Request)} if the typeServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeServiceDTO> updateTypeService(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody TypeServiceDTO typeServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TypeService : {}, {}", id, typeServiceDTO);
        if (typeServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        typeServiceDTO = typeServiceService.update(typeServiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeServiceDTO.getId().toString()))
            .body(typeServiceDTO);
    }

    /**
     * {@code PATCH  /type-services/:id} : Partial updates given fields of an existing typeService, field will ignore if it is null
     *
     * @param id the id of the typeServiceDTO to save.
     * @param typeServiceDTO the typeServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeServiceDTO,
     * or with status {@code 400 (Bad Request)} if the typeServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeServiceDTO> partialUpdateTypeService(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody TypeServiceDTO typeServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update TypeService : {}, {}", id, typeServiceDTO);
        if (typeServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeServiceDTO> result = typeServiceService.partialUpdate(typeServiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeServiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-services} : get all the Type Services.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Type Services in body.
     */
    @GetMapping("")
    public List<TypeServiceDTO> getAllTypeServices() {
        LOG.debug("REST request to get all TypeServices");
        return typeServiceService.findAll();
    }

    /**
     * {@code GET  /type-services/:id} : get the "id" typeService.
     *
     * @param id the id of the typeServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeServiceDTO> getTypeService(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get TypeService : {}", id);
        Optional<TypeServiceDTO> typeServiceDTO = typeServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeServiceDTO);
    }

    /**
     * {@code DELETE  /type-services/:id} : delete the "id" typeService.
     *
     * @param id the id of the typeServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeService(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete TypeService : {}", id);
        typeServiceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
