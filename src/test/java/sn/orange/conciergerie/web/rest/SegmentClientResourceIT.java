package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.SegmentClientAsserts.*;
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
import sn.orange.conciergerie.domain.SegmentClient;
import sn.orange.conciergerie.repository.SegmentClientRepository;
import sn.orange.conciergerie.service.dto.SegmentClientDTO;
import sn.orange.conciergerie.service.mapper.SegmentClientMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link SegmentClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SegmentClientResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/segment-clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SegmentClientRepository segmentClientRepository;

    @Autowired
    private SegmentClientMapper segmentClientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSegmentClientMockMvc;

    private SegmentClient segmentClient;

    private SegmentClient insertedSegmentClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SegmentClient createEntity() {
        return new SegmentClient().code(DEFAULT_CODE).libelle(DEFAULT_LIBELLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SegmentClient createUpdatedEntity() {
        return new SegmentClient().code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
    }

    @BeforeEach
    void initTest() {
        segmentClient = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSegmentClient != null) {
            segmentClientRepository.delete(insertedSegmentClient);
            insertedSegmentClient = null;
        }
    }

    @Test
    @Transactional
    void createSegmentClient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SegmentClient
        SegmentClientDTO segmentClientDTO = segmentClientMapper.toDto(segmentClient);
        var returnedSegmentClientDTO = om.readValue(
            restSegmentClientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(segmentClientDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SegmentClientDTO.class
        );

        // Validate the SegmentClient in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSegmentClient = segmentClientMapper.toEntity(returnedSegmentClientDTO);
        assertSegmentClientUpdatableFieldsEquals(returnedSegmentClient, getPersistedSegmentClient(returnedSegmentClient));

        insertedSegmentClient = returnedSegmentClient;
    }

    @Test
    @Transactional
    void createSegmentClientWithExistingId() throws Exception {
        // Create the SegmentClient with an existing ID
        insertedSegmentClient = segmentClientRepository.saveAndFlush(segmentClient);
        SegmentClientDTO segmentClientDTO = segmentClientMapper.toDto(segmentClient);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSegmentClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(segmentClientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SegmentClient in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        segmentClient.setCode(null);

        // Create the SegmentClient, which fails.
        SegmentClientDTO segmentClientDTO = segmentClientMapper.toDto(segmentClient);

        restSegmentClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(segmentClientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        segmentClient.setLibelle(null);

        // Create the SegmentClient, which fails.
        SegmentClientDTO segmentClientDTO = segmentClientMapper.toDto(segmentClient);

        restSegmentClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(segmentClientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSegmentClients() throws Exception {
        // Initialize the database
        insertedSegmentClient = segmentClientRepository.saveAndFlush(segmentClient);

        // Get all the segmentClientList
        restSegmentClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(segmentClient.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getSegmentClient() throws Exception {
        // Initialize the database
        insertedSegmentClient = segmentClientRepository.saveAndFlush(segmentClient);

        // Get the segmentClient
        restSegmentClientMockMvc
            .perform(get(ENTITY_API_URL_ID, segmentClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(segmentClient.getId().toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingSegmentClient() throws Exception {
        // Get the segmentClient
        restSegmentClientMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSegmentClient() throws Exception {
        // Initialize the database
        insertedSegmentClient = segmentClientRepository.saveAndFlush(segmentClient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the segmentClient
        SegmentClient updatedSegmentClient = segmentClientRepository.findById(segmentClient.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSegmentClient are not directly saved in db
        em.detach(updatedSegmentClient);
        updatedSegmentClient.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
        SegmentClientDTO segmentClientDTO = segmentClientMapper.toDto(updatedSegmentClient);

        restSegmentClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, segmentClientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(segmentClientDTO))
            )
            .andExpect(status().isOk());

        // Validate the SegmentClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSegmentClientToMatchAllProperties(updatedSegmentClient);
    }

    @Test
    @Transactional
    void putNonExistingSegmentClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        segmentClient.setId(UUID.randomUUID());

        // Create the SegmentClient
        SegmentClientDTO segmentClientDTO = segmentClientMapper.toDto(segmentClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSegmentClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, segmentClientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(segmentClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SegmentClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSegmentClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        segmentClient.setId(UUID.randomUUID());

        // Create the SegmentClient
        SegmentClientDTO segmentClientDTO = segmentClientMapper.toDto(segmentClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSegmentClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(segmentClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SegmentClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSegmentClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        segmentClient.setId(UUID.randomUUID());

        // Create the SegmentClient
        SegmentClientDTO segmentClientDTO = segmentClientMapper.toDto(segmentClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSegmentClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(segmentClientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SegmentClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSegmentClientWithPatch() throws Exception {
        // Initialize the database
        insertedSegmentClient = segmentClientRepository.saveAndFlush(segmentClient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the segmentClient using partial update
        SegmentClient partialUpdatedSegmentClient = new SegmentClient();
        partialUpdatedSegmentClient.setId(segmentClient.getId());

        partialUpdatedSegmentClient.libelle(UPDATED_LIBELLE);

        restSegmentClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSegmentClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSegmentClient))
            )
            .andExpect(status().isOk());

        // Validate the SegmentClient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSegmentClientUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSegmentClient, segmentClient),
            getPersistedSegmentClient(segmentClient)
        );
    }

    @Test
    @Transactional
    void fullUpdateSegmentClientWithPatch() throws Exception {
        // Initialize the database
        insertedSegmentClient = segmentClientRepository.saveAndFlush(segmentClient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the segmentClient using partial update
        SegmentClient partialUpdatedSegmentClient = new SegmentClient();
        partialUpdatedSegmentClient.setId(segmentClient.getId());

        partialUpdatedSegmentClient.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restSegmentClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSegmentClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSegmentClient))
            )
            .andExpect(status().isOk());

        // Validate the SegmentClient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSegmentClientUpdatableFieldsEquals(partialUpdatedSegmentClient, getPersistedSegmentClient(partialUpdatedSegmentClient));
    }

    @Test
    @Transactional
    void patchNonExistingSegmentClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        segmentClient.setId(UUID.randomUUID());

        // Create the SegmentClient
        SegmentClientDTO segmentClientDTO = segmentClientMapper.toDto(segmentClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSegmentClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, segmentClientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(segmentClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SegmentClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSegmentClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        segmentClient.setId(UUID.randomUUID());

        // Create the SegmentClient
        SegmentClientDTO segmentClientDTO = segmentClientMapper.toDto(segmentClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSegmentClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(segmentClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SegmentClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSegmentClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        segmentClient.setId(UUID.randomUUID());

        // Create the SegmentClient
        SegmentClientDTO segmentClientDTO = segmentClientMapper.toDto(segmentClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSegmentClientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(segmentClientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SegmentClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSegmentClient() throws Exception {
        // Initialize the database
        insertedSegmentClient = segmentClientRepository.saveAndFlush(segmentClient);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the segmentClient
        restSegmentClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, segmentClient.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return segmentClientRepository.count();
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

    protected SegmentClient getPersistedSegmentClient(SegmentClient segmentClient) {
        return segmentClientRepository.findById(segmentClient.getId()).orElseThrow();
    }

    protected void assertPersistedSegmentClientToMatchAllProperties(SegmentClient expectedSegmentClient) {
        assertSegmentClientAllPropertiesEquals(expectedSegmentClient, getPersistedSegmentClient(expectedSegmentClient));
    }

    protected void assertPersistedSegmentClientToMatchUpdatableProperties(SegmentClient expectedSegmentClient) {
        assertSegmentClientAllUpdatablePropertiesEquals(expectedSegmentClient, getPersistedSegmentClient(expectedSegmentClient));
    }
}
