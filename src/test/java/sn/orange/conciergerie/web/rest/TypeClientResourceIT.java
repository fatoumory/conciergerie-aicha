package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.TypeClientAsserts.*;
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
import sn.orange.conciergerie.domain.TypeClient;
import sn.orange.conciergerie.repository.TypeClientRepository;
import sn.orange.conciergerie.service.dto.TypeClientDTO;
import sn.orange.conciergerie.service.mapper.TypeClientMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link TypeClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeClientResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TypeClientRepository typeClientRepository;

    @Autowired
    private TypeClientMapper typeClientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeClientMockMvc;

    private TypeClient typeClient;

    private TypeClient insertedTypeClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeClient createEntity() {
        return new TypeClient().code(DEFAULT_CODE).libelle(DEFAULT_LIBELLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeClient createUpdatedEntity() {
        return new TypeClient().code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
    }

    @BeforeEach
    void initTest() {
        typeClient = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTypeClient != null) {
            typeClientRepository.delete(insertedTypeClient);
            insertedTypeClient = null;
        }
    }

    @Test
    @Transactional
    void createTypeClient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TypeClient
        TypeClientDTO typeClientDTO = typeClientMapper.toDto(typeClient);
        var returnedTypeClientDTO = om.readValue(
            restTypeClientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeClientDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TypeClientDTO.class
        );

        // Validate the TypeClient in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTypeClient = typeClientMapper.toEntity(returnedTypeClientDTO);
        assertTypeClientUpdatableFieldsEquals(returnedTypeClient, getPersistedTypeClient(returnedTypeClient));

        insertedTypeClient = returnedTypeClient;
    }

    @Test
    @Transactional
    void createTypeClientWithExistingId() throws Exception {
        // Create the TypeClient with an existing ID
        insertedTypeClient = typeClientRepository.saveAndFlush(typeClient);
        TypeClientDTO typeClientDTO = typeClientMapper.toDto(typeClient);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeClientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeClient in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeClient.setCode(null);

        // Create the TypeClient, which fails.
        TypeClientDTO typeClientDTO = typeClientMapper.toDto(typeClient);

        restTypeClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeClientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeClient.setLibelle(null);

        // Create the TypeClient, which fails.
        TypeClientDTO typeClientDTO = typeClientMapper.toDto(typeClient);

        restTypeClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeClientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeClients() throws Exception {
        // Initialize the database
        insertedTypeClient = typeClientRepository.saveAndFlush(typeClient);

        // Get all the typeClientList
        restTypeClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeClient.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getTypeClient() throws Exception {
        // Initialize the database
        insertedTypeClient = typeClientRepository.saveAndFlush(typeClient);

        // Get the typeClient
        restTypeClientMockMvc
            .perform(get(ENTITY_API_URL_ID, typeClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeClient.getId().toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingTypeClient() throws Exception {
        // Get the typeClient
        restTypeClientMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeClient() throws Exception {
        // Initialize the database
        insertedTypeClient = typeClientRepository.saveAndFlush(typeClient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeClient
        TypeClient updatedTypeClient = typeClientRepository.findById(typeClient.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeClient are not directly saved in db
        em.detach(updatedTypeClient);
        updatedTypeClient.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
        TypeClientDTO typeClientDTO = typeClientMapper.toDto(updatedTypeClient);

        restTypeClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeClientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeClientDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTypeClientToMatchAllProperties(updatedTypeClient);
    }

    @Test
    @Transactional
    void putNonExistingTypeClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeClient.setId(UUID.randomUUID());

        // Create the TypeClient
        TypeClientDTO typeClientDTO = typeClientMapper.toDto(typeClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeClientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeClient.setId(UUID.randomUUID());

        // Create the TypeClient
        TypeClientDTO typeClientDTO = typeClientMapper.toDto(typeClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeClient.setId(UUID.randomUUID());

        // Create the TypeClient
        TypeClientDTO typeClientDTO = typeClientMapper.toDto(typeClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeClientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeClientWithPatch() throws Exception {
        // Initialize the database
        insertedTypeClient = typeClientRepository.saveAndFlush(typeClient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeClient using partial update
        TypeClient partialUpdatedTypeClient = new TypeClient();
        partialUpdatedTypeClient.setId(typeClient.getId());

        restTypeClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeClient))
            )
            .andExpect(status().isOk());

        // Validate the TypeClient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeClientUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTypeClient, typeClient),
            getPersistedTypeClient(typeClient)
        );
    }

    @Test
    @Transactional
    void fullUpdateTypeClientWithPatch() throws Exception {
        // Initialize the database
        insertedTypeClient = typeClientRepository.saveAndFlush(typeClient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeClient using partial update
        TypeClient partialUpdatedTypeClient = new TypeClient();
        partialUpdatedTypeClient.setId(typeClient.getId());

        partialUpdatedTypeClient.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restTypeClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeClient))
            )
            .andExpect(status().isOk());

        // Validate the TypeClient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeClientUpdatableFieldsEquals(partialUpdatedTypeClient, getPersistedTypeClient(partialUpdatedTypeClient));
    }

    @Test
    @Transactional
    void patchNonExistingTypeClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeClient.setId(UUID.randomUUID());

        // Create the TypeClient
        TypeClientDTO typeClientDTO = typeClientMapper.toDto(typeClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeClientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeClient.setId(UUID.randomUUID());

        // Create the TypeClient
        TypeClientDTO typeClientDTO = typeClientMapper.toDto(typeClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeClient.setId(UUID.randomUUID());

        // Create the TypeClient
        TypeClientDTO typeClientDTO = typeClientMapper.toDto(typeClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeClientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(typeClientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeClient() throws Exception {
        // Initialize the database
        insertedTypeClient = typeClientRepository.saveAndFlush(typeClient);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the typeClient
        restTypeClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeClient.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return typeClientRepository.count();
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

    protected TypeClient getPersistedTypeClient(TypeClient typeClient) {
        return typeClientRepository.findById(typeClient.getId()).orElseThrow();
    }

    protected void assertPersistedTypeClientToMatchAllProperties(TypeClient expectedTypeClient) {
        assertTypeClientAllPropertiesEquals(expectedTypeClient, getPersistedTypeClient(expectedTypeClient));
    }

    protected void assertPersistedTypeClientToMatchUpdatableProperties(TypeClient expectedTypeClient) {
        assertTypeClientAllUpdatablePropertiesEquals(expectedTypeClient, getPersistedTypeClient(expectedTypeClient));
    }
}
