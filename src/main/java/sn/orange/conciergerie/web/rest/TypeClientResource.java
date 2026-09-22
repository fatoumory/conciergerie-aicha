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
import sn.orange.conciergerie.repository.TypeClientRepository;
import sn.orange.conciergerie.service.TypeClientService;
import sn.orange.conciergerie.service.dto.TypeClientDTO;
import sn.orange.conciergerie.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.orange.conciergerie.domain.TypeClient}.
 */
@RestController
@RequestMapping("/api/type-clients")
public class TypeClientResource {

    private static final Logger LOG = LoggerFactory.getLogger(TypeClientResource.class);

    private static final String ENTITY_NAME = "typeClient";

    @Value("${jhipster.clientApp.name:conciergerie}")
    private String applicationName;

    private final TypeClientService typeClientService;

    private final TypeClientRepository typeClientRepository;

    public TypeClientResource(TypeClientService typeClientService, TypeClientRepository typeClientRepository) {
        this.typeClientService = typeClientService;
        this.typeClientRepository = typeClientRepository;
    }

    /**
     * {@code POST  /type-clients} : Create a new typeClient.
     *
     * @param typeClientDTO the typeClientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeClientDTO, or with status {@code 400 (Bad Request)} if the typeClient already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeClientDTO> createTypeClient(@Valid @RequestBody TypeClientDTO typeClientDTO) throws URISyntaxException {
        LOG.debug("REST request to save TypeClient : {}", typeClientDTO);
        if (typeClientDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeClient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        typeClientDTO = typeClientService.save(typeClientDTO);
        return ResponseEntity.created(new URI("/api/type-clients/" + typeClientDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, typeClientDTO.getId().toString()))
            .body(typeClientDTO);
    }

    /**
     * {@code PUT  /type-clients/:id} : Updates an existing typeClient.
     *
     * @param id the id of the typeClientDTO to save.
     * @param typeClientDTO the typeClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeClientDTO,
     * or with status {@code 400 (Bad Request)} if the typeClientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeClientDTO> updateTypeClient(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody TypeClientDTO typeClientDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TypeClient : {}, {}", id, typeClientDTO);
        if (typeClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        typeClientDTO = typeClientService.update(typeClientDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeClientDTO.getId().toString()))
            .body(typeClientDTO);
    }

    /**
     * {@code PATCH  /type-clients/:id} : Partial updates given fields of an existing typeClient, field will ignore if it is null
     *
     * @param id the id of the typeClientDTO to save.
     * @param typeClientDTO the typeClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeClientDTO,
     * or with status {@code 400 (Bad Request)} if the typeClientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeClientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeClientDTO> partialUpdateTypeClient(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody TypeClientDTO typeClientDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update TypeClient : {}, {}", id, typeClientDTO);
        if (typeClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeClientDTO> result = typeClientService.partialUpdate(typeClientDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeClientDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-clients} : get all the Type Clients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Type Clients in body.
     */
    @GetMapping("")
    public List<TypeClientDTO> getAllTypeClients() {
        LOG.debug("REST request to get all TypeClients");
        return typeClientService.findAll();
    }

    /**
     * {@code GET  /type-clients/:id} : get the "id" typeClient.
     *
     * @param id the id of the typeClientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeClientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeClientDTO> getTypeClient(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get TypeClient : {}", id);
        Optional<TypeClientDTO> typeClientDTO = typeClientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeClientDTO);
    }

    /**
     * {@code DELETE  /type-clients/:id} : delete the "id" typeClient.
     *
     * @param id the id of the typeClientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeClient(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete TypeClient : {}", id);
        typeClientService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
