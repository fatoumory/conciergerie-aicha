package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.TypeServiceAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.IntegrationTest;
import sn.orange.conciergerie.domain.TypeService;
import sn.orange.conciergerie.repository.TypeServiceRepository;
import sn.orange.conciergerie.service.dto.TypeServiceDTO;
import sn.orange.conciergerie.service.mapper.TypeServiceMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link TypeServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeServiceResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TypeServiceRepository typeServiceRepository;

    @Autowired
    private TypeServiceMapper typeServiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeServiceMockMvc;

    private TypeService typeService;

    private TypeService insertedTypeService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeService createEntity() {
        return new TypeService().code(DEFAULT_CODE).libelle(DEFAULT_LIBELLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeService createUpdatedEntity() {
        return new TypeService().code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
    }

    @BeforeEach
    void initTest() {
        typeService = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTypeService != null) {
            typeServiceRepository.delete(insertedTypeService);
            insertedTypeService = null;
        }
    }

    @Test
    @Transactional
    void createTypeService() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TypeService
        TypeServiceDTO typeServiceDTO = typeServiceMapper.toDto(typeService);
        var returnedTypeServiceDTO = om.readValue(
            restTypeServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeServiceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TypeServiceDTO.class
        );

        // Validate the TypeService in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTypeService = typeServiceMapper.toEntity(returnedTypeServiceDTO);
        assertTypeServiceUpdatableFieldsEquals(returnedTypeService, getPersistedTypeService(returnedTypeService));

        insertedTypeService = returnedTypeService;
    }

    @Test
    @Transactional
    void createTypeServiceWithExistingId() throws Exception {
        // Create the TypeService with an existing ID
        insertedTypeService = typeServiceRepository.saveAndFlush(typeService);
        TypeServiceDTO typeServiceDTO = typeServiceMapper.toDto(typeService);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeService in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeService.setCode(null);

        // Create the TypeService, which fails.
        TypeServiceDTO typeServiceDTO = typeServiceMapper.toDto(typeService);

        restTypeServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeService.setLibelle(null);

        // Create the TypeService, which fails.
        TypeServiceDTO typeServiceDTO = typeServiceMapper.toDto(typeService);

        restTypeServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeServices() throws Exception {
        // Initialize the database
        insertedTypeService = typeServiceRepository.saveAndFlush(typeService);

        // Get all the typeServiceList
        restTypeServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeService.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getTypeService() throws Exception {
        // Initialize the database
        insertedTypeService = typeServiceRepository.saveAndFlush(typeService);

        // Get the typeService
        restTypeServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, typeService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeService.getId().toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingTypeService() throws Exception {
        // Get the typeService
        restTypeServiceMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeService() throws Exception {
        // Initialize the database
        insertedTypeService = typeServiceRepository.saveAndFlush(typeService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeService
        TypeService updatedTypeService = typeServiceRepository.findById(typeService.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeService are not directly saved in db
        em.detach(updatedTypeService);
        updatedTypeService.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
        TypeServiceDTO typeServiceDTO = typeServiceMapper.toDto(updatedTypeService);

        restTypeServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTypeServiceToMatchAllProperties(updatedTypeService);
    }

    @Test
    @Transactional
    void putNonExistingTypeService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeService.setId(UUID.randomUUID());

        // Create the TypeService
        TypeServiceDTO typeServiceDTO = typeServiceMapper.toDto(typeService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeService.setId(UUID.randomUUID());

        // Create the TypeService
        TypeServiceDTO typeServiceDTO = typeServiceMapper.toDto(typeService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeService.setId(UUID.randomUUID());

        // Create the TypeService
        TypeServiceDTO typeServiceDTO = typeServiceMapper.toDto(typeService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeServiceWithPatch() throws Exception {
        // Initialize the database
        insertedTypeService = typeServiceRepository.saveAndFlush(typeService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeService using partial update
        TypeService partialUpdatedTypeService = new TypeService();
        partialUpdatedTypeService.setId(typeService.getId());

        partialUpdatedTypeService.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restTypeServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeService))
            )
            .andExpect(status().isOk());

        // Validate the TypeService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeServiceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTypeService, typeService),
            getPersistedTypeService(typeService)
        );
    }

    @Test
    @Transactional
    void fullUpdateTypeServiceWithPatch() throws Exception {
        // Initialize the database
        insertedTypeService = typeServiceRepository.saveAndFlush(typeService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeService using partial update
        TypeService partialUpdatedTypeService = new TypeService();
        partialUpdatedTypeService.setId(typeService.getId());

        partialUpdatedTypeService.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restTypeServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeService))
            )
            .andExpect(status().isOk());

        // Validate the TypeService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeServiceUpdatableFieldsEquals(partialUpdatedTypeService, getPersistedTypeService(partialUpdatedTypeService));
    }

    @Test
    @Transactional
    void patchNonExistingTypeService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeService.setId(UUID.randomUUID());

        // Create the TypeService
        TypeServiceDTO typeServiceDTO = typeServiceMapper.toDto(typeService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeService.setId(UUID.randomUUID());

        // Create the TypeService
        TypeServiceDTO typeServiceDTO = typeServiceMapper.toDto(typeService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeService.setId(UUID.randomUUID());

        // Create the TypeService
        TypeServiceDTO typeServiceDTO = typeServiceMapper.toDto(typeService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeServiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(typeServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeService() throws Exception {
        // Initialize the database
        insertedTypeService = typeServiceRepository.saveAndFlush(typeService);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the typeService
        restTypeServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeService.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return typeServiceRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected TypeService getPersistedTypeService(TypeService typeService) {
        return typeServiceRepository.findById(typeService.getId()).orElseThrow();
    }

    protected void assertPersistedTypeServiceToMatchAllProperties(TypeService expectedTypeService) {
        assertTypeServiceAllPropertiesEquals(expectedTypeService, getPersistedTypeService(expectedTypeService));
    }

    protected void assertPersistedTypeServiceToMatchUpdatableProperties(TypeService expectedTypeService) {
        assertTypeServiceAllUpdatablePropertiesEquals(expectedTypeService, getPersistedTypeService(expectedTypeService));
    }
}
