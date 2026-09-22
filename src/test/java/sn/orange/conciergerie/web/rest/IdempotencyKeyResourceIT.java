package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.IdempotencyKeyAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
import java.time.Instant;
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
import sn.orange.conciergerie.domain.IdempotencyKey;
import sn.orange.conciergerie.repository.IdempotencyKeyRepository;
import sn.orange.conciergerie.service.dto.IdempotencyKeyDTO;
import sn.orange.conciergerie.service.mapper.IdempotencyKeyMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link IdempotencyKeyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IdempotencyKeyResourceIT {

    private static final String DEFAULT_CLE = "AAAAAAAAAA";
    private static final String UPDATED_CLE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_OPERATION = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_OPERATION = "BBBBBBBBBB";

    private static final UUID DEFAULT_RESOURCE_ID = UUID.randomUUID();
    private static final UUID UPDATED_RESOURCE_ID = UUID.randomUUID();

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.ofEpochMilli(1701862474321L);

    private static final Instant DEFAULT_DATE_EXPIRATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_EXPIRATION = Instant.ofEpochMilli(1701862474321L);

    private static final String ENTITY_API_URL = "/api/idempotency-keys";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IdempotencyKeyRepository idempotencyKeyRepository;

    @Autowired
    private IdempotencyKeyMapper idempotencyKeyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIdempotencyKeyMockMvc;

    private IdempotencyKey idempotencyKey;

    private IdempotencyKey insertedIdempotencyKey;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdempotencyKey createEntity() {
        return new IdempotencyKey()
            .cle(DEFAULT_CLE)
            .typeOperation(DEFAULT_TYPE_OPERATION)
            .resourceId(DEFAULT_RESOURCE_ID)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateExpiration(DEFAULT_DATE_EXPIRATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdempotencyKey createUpdatedEntity() {
        return new IdempotencyKey()
            .cle(UPDATED_CLE)
            .typeOperation(UPDATED_TYPE_OPERATION)
            .resourceId(UPDATED_RESOURCE_ID)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateExpiration(UPDATED_DATE_EXPIRATION);
    }

    @BeforeEach
    void initTest() {
        idempotencyKey = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIdempotencyKey != null) {
            idempotencyKeyRepository.delete(insertedIdempotencyKey);
            insertedIdempotencyKey = null;
        }
    }

    @Test
    @Transactional
    void createIdempotencyKey() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IdempotencyKey
        IdempotencyKeyDTO idempotencyKeyDTO = idempotencyKeyMapper.toDto(idempotencyKey);
        var returnedIdempotencyKeyDTO = om.readValue(
            restIdempotencyKeyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idempotencyKeyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IdempotencyKeyDTO.class
        );

        // Validate the IdempotencyKey in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIdempotencyKey = idempotencyKeyMapper.toEntity(returnedIdempotencyKeyDTO);
        assertIdempotencyKeyUpdatableFieldsEquals(returnedIdempotencyKey, getPersistedIdempotencyKey(returnedIdempotencyKey));

        insertedIdempotencyKey = returnedIdempotencyKey;
    }

    @Test
    @Transactional
    void createIdempotencyKeyWithExistingId() throws Exception {
        // Create the IdempotencyKey with an existing ID
        insertedIdempotencyKey = idempotencyKeyRepository.saveAndFlush(idempotencyKey);
        IdempotencyKeyDTO idempotencyKeyDTO = idempotencyKeyMapper.toDto(idempotencyKey);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdempotencyKeyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idempotencyKeyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        idempotencyKey.setCle(null);

        // Create the IdempotencyKey, which fails.
        IdempotencyKeyDTO idempotencyKeyDTO = idempotencyKeyMapper.toDto(idempotencyKey);

        restIdempotencyKeyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idempotencyKeyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeOperationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        idempotencyKey.setTypeOperation(null);

        // Create the IdempotencyKey, which fails.
        IdempotencyKeyDTO idempotencyKeyDTO = idempotencyKeyMapper.toDto(idempotencyKey);

        restIdempotencyKeyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idempotencyKeyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        idempotencyKey.setDateCreation(null);

        // Create the IdempotencyKey, which fails.
        IdempotencyKeyDTO idempotencyKeyDTO = idempotencyKeyMapper.toDto(idempotencyKey);

        restIdempotencyKeyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idempotencyKeyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIdempotencyKeys() throws Exception {
        // Initialize the database
        insertedIdempotencyKey = idempotencyKeyRepository.saveAndFlush(idempotencyKey);

        // Get all the idempotencyKeyList
        restIdempotencyKeyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(idempotencyKey.getId().toString())))
            .andExpect(jsonPath("$.[*].cle").value(hasItem(DEFAULT_CLE)))
            .andExpect(jsonPath("$.[*].typeOperation").value(hasItem(DEFAULT_TYPE_OPERATION)))
            .andExpect(jsonPath("$.[*].resourceId").value(hasItem(DEFAULT_RESOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateExpiration").value(hasItem(DEFAULT_DATE_EXPIRATION.toString())));
    }

    @Test
    @Transactional
    void getIdempotencyKey() throws Exception {
        // Initialize the database
        insertedIdempotencyKey = idempotencyKeyRepository.saveAndFlush(idempotencyKey);

        // Get the idempotencyKey
        restIdempotencyKeyMockMvc
            .perform(get(ENTITY_API_URL_ID, idempotencyKey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(idempotencyKey.getId().toString()))
            .andExpect(jsonPath("$.cle").value(DEFAULT_CLE))
            .andExpect(jsonPath("$.typeOperation").value(DEFAULT_TYPE_OPERATION))
            .andExpect(jsonPath("$.resourceId").value(DEFAULT_RESOURCE_ID.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateExpiration").value(DEFAULT_DATE_EXPIRATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingIdempotencyKey() throws Exception {
        // Get the idempotencyKey
        restIdempotencyKeyMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIdempotencyKey() throws Exception {
        // Initialize the database
        insertedIdempotencyKey = idempotencyKeyRepository.saveAndFlush(idempotencyKey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the idempotencyKey
        IdempotencyKey updatedIdempotencyKey = idempotencyKeyRepository.findById(idempotencyKey.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIdempotencyKey are not directly saved in db
        em.detach(updatedIdempotencyKey);
        updatedIdempotencyKey
            .cle(UPDATED_CLE)
            .typeOperation(UPDATED_TYPE_OPERATION)
            .resourceId(UPDATED_RESOURCE_ID)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateExpiration(UPDATED_DATE_EXPIRATION);
        IdempotencyKeyDTO idempotencyKeyDTO = idempotencyKeyMapper.toDto(updatedIdempotencyKey);

        restIdempotencyKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, idempotencyKeyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(idempotencyKeyDTO))
            )
            .andExpect(status().isOk());

        // Validate the IdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIdempotencyKeyToMatchAllProperties(updatedIdempotencyKey);
    }

    @Test
    @Transactional
    void putNonExistingIdempotencyKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idempotencyKey.setId(UUID.randomUUID());

        // Create the IdempotencyKey
        IdempotencyKeyDTO idempotencyKeyDTO = idempotencyKeyMapper.toDto(idempotencyKey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdempotencyKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, idempotencyKeyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(idempotencyKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIdempotencyKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idempotencyKey.setId(UUID.randomUUID());

        // Create the IdempotencyKey
        IdempotencyKeyDTO idempotencyKeyDTO = idempotencyKeyMapper.toDto(idempotencyKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdempotencyKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(idempotencyKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIdempotencyKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idempotencyKey.setId(UUID.randomUUID());

        // Create the IdempotencyKey
        IdempotencyKeyDTO idempotencyKeyDTO = idempotencyKeyMapper.toDto(idempotencyKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdempotencyKeyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idempotencyKeyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIdempotencyKeyWithPatch() throws Exception {
        // Initialize the database
        insertedIdempotencyKey = idempotencyKeyRepository.saveAndFlush(idempotencyKey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the idempotencyKey using partial update
        IdempotencyKey partialUpdatedIdempotencyKey = new IdempotencyKey();
        partialUpdatedIdempotencyKey.setId(idempotencyKey.getId());

        restIdempotencyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdempotencyKey.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdempotencyKey))
            )
            .andExpect(status().isOk());

        // Validate the IdempotencyKey in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdempotencyKeyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIdempotencyKey, idempotencyKey),
            getPersistedIdempotencyKey(idempotencyKey)
        );
    }

    @Test
    @Transactional
    void fullUpdateIdempotencyKeyWithPatch() throws Exception {
        // Initialize the database
        insertedIdempotencyKey = idempotencyKeyRepository.saveAndFlush(idempotencyKey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the idempotencyKey using partial update
        IdempotencyKey partialUpdatedIdempotencyKey = new IdempotencyKey();
        partialUpdatedIdempotencyKey.setId(idempotencyKey.getId());

        partialUpdatedIdempotencyKey
            .cle(UPDATED_CLE)
            .typeOperation(UPDATED_TYPE_OPERATION)
            .resourceId(UPDATED_RESOURCE_ID)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateExpiration(UPDATED_DATE_EXPIRATION);

        restIdempotencyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdempotencyKey.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdempotencyKey))
            )
            .andExpect(status().isOk());

        // Validate the IdempotencyKey in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdempotencyKeyUpdatableFieldsEquals(partialUpdatedIdempotencyKey, getPersistedIdempotencyKey(partialUpdatedIdempotencyKey));
    }

    @Test
    @Transactional
    void patchNonExistingIdempotencyKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idempotencyKey.setId(UUID.randomUUID());

        // Create the IdempotencyKey
        IdempotencyKeyDTO idempotencyKeyDTO = idempotencyKeyMapper.toDto(idempotencyKey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdempotencyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, idempotencyKeyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(idempotencyKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIdempotencyKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idempotencyKey.setId(UUID.randomUUID());

        // Create the IdempotencyKey
        IdempotencyKeyDTO idempotencyKeyDTO = idempotencyKeyMapper.toDto(idempotencyKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdempotencyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(idempotencyKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIdempotencyKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idempotencyKey.setId(UUID.randomUUID());

        // Create the IdempotencyKey
        IdempotencyKeyDTO idempotencyKeyDTO = idempotencyKeyMapper.toDto(idempotencyKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdempotencyKeyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(idempotencyKeyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIdempotencyKey() throws Exception {
        // Initialize the database
        insertedIdempotencyKey = idempotencyKeyRepository.saveAndFlush(idempotencyKey);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the idempotencyKey
        restIdempotencyKeyMockMvc
            .perform(delete(ENTITY_API_URL_ID, idempotencyKey.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return idempotencyKeyRepository.count();
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

    protected IdempotencyKey getPersistedIdempotencyKey(IdempotencyKey idempotencyKey) {
        return idempotencyKeyRepository.findById(idempotencyKey.getId()).orElseThrow();
    }

    protected void assertPersistedIdempotencyKeyToMatchAllProperties(IdempotencyKey expectedIdempotencyKey) {
        assertIdempotencyKeyAllPropertiesEquals(expectedIdempotencyKey, getPersistedIdempotencyKey(expectedIdempotencyKey));
    }

    protected void assertPersistedIdempotencyKeyToMatchUpdatableProperties(IdempotencyKey expectedIdempotencyKey) {
        assertIdempotencyKeyAllUpdatablePropertiesEquals(expectedIdempotencyKey, getPersistedIdempotencyKey(expectedIdempotencyKey));
    }
}
